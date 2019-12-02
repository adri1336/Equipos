package com.example.equipos.model.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable
{
    private Long id;
    private String firstname, lastname;
    private Long id_team;

    public Player(String firstname, String lastname, Long id_team)
    {
        this.firstname = firstname;
        this.lastname = lastname;
        this.id_team = id_team;
    }

    protected Player(Parcel in)
    {
        if(in.readByte() == 0) { id = null; }
        else { id = in.readLong(); }
        firstname = in.readString();
        lastname = in.readString();
        if(in.readByte() == 0) { id_team = null; }
        else { id_team = in.readLong(); }
    }

    public static final Creator<Player> CREATOR = new Creator<Player>()
    {
        @Override
        public Player createFromParcel(Parcel in)
        {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size)
        {
            return new Player[size];
        }
    };

    public long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public long getId_team()
    {
        return id_team;
    }

    public void setId_team(Long id_team)
    {
        this.id_team = id_team;
    }

    @Override
    public String toString()
    {
        return "Player{" + "id=" + id + ", firstname='" + firstname + '\'' + ", lastname='" + lastname + '\'' + ", id_team=" + id_team + '}';
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        if(id == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(firstname);
        dest.writeString(lastname);
        if(id_team == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeLong(id_team);
        }
    }
}
