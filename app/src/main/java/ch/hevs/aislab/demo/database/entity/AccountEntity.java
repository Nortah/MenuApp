package ch.hevs.aislab.demo.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import ch.hevs.aislab.demo.model.Account;

/**
 * https://developer.android.com/reference/android/arch/persistence/room/Entity.html
 *
 * interesting: owner column references a foreign key, that's why this column is indexed.
 * If not indexed, it might trigger full table scans whenever parent table is modified so you are
 * highly advised to create an index that covers this column.
 *
 * Further information to Parcelable:
 * https://developer.android.com/reference/android/os/Parcelable
 * Why we use Parcelable over Serializable:
 * https://android.jlelse.eu/parcelable-vs-serializable-6a2556d51538
 */
@Entity(tableName = "accounts",
        foreignKeys =
        @ForeignKey(
                entity = ClientEntity.class,
                parentColumns = "email",
                childColumns = "owner",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
        @Index(
                value = {"owner"}
        )}
)
public class AccountEntity implements Account, Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private Double balance;
    private String owner;

    public AccountEntity() {
    }

    public AccountEntity(Account account) {
        id = account.getId();
        name = account.getName();
        balance = account.getBalance();
        owner = account.getOwner();
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof AccountEntity)) return false;
        AccountEntity o = (AccountEntity) obj;
        return o.getId().equals(this.getId());
    }

    // Everything below this is part of the Parcelable implementation:
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        // The order here must be mirrored in the parcel constructor!
        out.writeLong(id);
        out.writeString(name);
        out.writeDouble(balance);
        out.writeString(owner);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<AccountEntity> CREATOR = new Parcelable.Creator<AccountEntity>() {
        public AccountEntity createFromParcel(Parcel in) {
            return new AccountEntity(in);
        }

        public AccountEntity[] newArray(int size) {
            return new AccountEntity[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private AccountEntity(Parcel in) {
        // This order must be the same as in the "writeToParcel" method!
        id = in.readLong();
        name = in.readString();
        balance = in.readDouble();
        owner = in.readString();
    }
}
