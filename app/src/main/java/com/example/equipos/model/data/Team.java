package com.example.equipos.model.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Team implements Parcelable
{
    private Long id;
    private String name, city, stadium;
    private Integer stadium_capacity;

    public Team(String name, String city, String stadium, Integer stadium_capacity)
    {
        this.name = name;
        this.city = city;
        this.stadium = stadium;
        this.stadium_capacity = stadium_capacity;
    }

    protected Team(Parcel in)
    {
        if(in.readByte() == 0) { id = null; }
        else { id = in.readLong(); }
        name = in.readString();
        city = in.readString();
        stadium = in.readString();
        if(in.readByte() == 0) { stadium_capacity = null; }
        else { stadium_capacity = in.readInt(); }
    }

    public static final Creator<Team> CREATOR = new Creator<Team>()
    {
        @Override
        public Team createFromParcel(Parcel in)
        {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size)
        {
            return new Team[size];
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getStadium()
    {
        return stadium;
    }

    public void setStadium(String stadium)
    {
        this.stadium = stadium;
    }

    public int getStadium_capacity()
    {
        return stadium_capacity;
    }

    public void setStadium_capacity(Integer stadium_capacity)
    {
        this.stadium_capacity = stadium_capacity;
    }

    @Override
    public String toString()
    {
        return "Team{" + "id=" + id + ", name='" + name + '\'' + ", city='" + city + '\'' + ", stadium='" + stadium + '\'' + ", stadium_capacity=" + stadium_capacity + '}';
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
        dest.writeString(name);
        dest.writeString(city);
        dest.writeString(stadium);
        if(stadium_capacity == null) { dest.writeByte((byte) 0); }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeInt(stadium_capacity);
        }
    }
}
