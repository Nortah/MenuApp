package ch.hevs.aislab.demo.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

import ch.hevs.aislab.demo.model.Client;

/**
 * https://developer.android.com/reference/android/arch/persistence/room/Entity.html
 *
 * Further information to Parcelable:
 * https://developer.android.com/reference/android/os/Parcelable
 * Why we use Parcelable over Serializable:
 * https://android.jlelse.eu/parcelable-vs-serializable-6a2556d51538
 */
@Entity(tableName = "clients", primaryKeys = {"email"})
public class ClientEntity implements Client, Parcelable {

    @NonNull
    private String email;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    private String password;

    public ClientEntity() {
    }

    public ClientEntity(Client client) {
        email = client.getEmail();
        firstName = client.getFirstName();
        lastName = client.getLastName();
        password = client.getPassword();
    }

    public ClientEntity(@NonNull String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    @NonNull
    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ClientEntity)) return false;
        ClientEntity o = (ClientEntity) obj;
        return o.getEmail().equals(this.getEmail());
    }

    // Everything below this is part of the Parcelable implementation:
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        // The order here must be mirrored in the parcel constructor!
        out.writeString(email);
        out.writeString(firstName);
        out.writeString(lastName);
        out.writeString(password);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<ClientEntity> CREATOR = new Parcelable.Creator<ClientEntity>() {
        public ClientEntity createFromParcel(Parcel in) {
            return new ClientEntity(in);
        }

        public ClientEntity[] newArray(int size) {
            return new ClientEntity[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private ClientEntity(Parcel in) {
        // This order must be the same as in the "writeToParcel" method!
        email = in.readString();
        firstName= in.readString();
        lastName = in.readString();
        password = in.readString();
    }
}
