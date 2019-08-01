package it.unibs.dii.pajc.pig.client.utility;

import java.io.Serializable;
import java.util.Date;

public class ServerConnectionData implements Serializable {
    private boolean favorite;
    private String address;
    private String description;
    private Date lastConnection;

    public static final String SINGLE_BYTE_IP_REGEX = "([1-9]?[0-9]|(1[0-9]|2[0-4])[0-9]|25[0-5])";
    public static final String IPV4_REGEX_VERIFIER = SINGLE_BYTE_IP_REGEX+"\\."+SINGLE_BYTE_IP_REGEX+"\\."+SINGLE_BYTE_IP_REGEX+"\\."+SINGLE_BYTE_IP_REGEX;

    public ServerConnectionData() {
        this(null);
    }

    public ServerConnectionData(String address) {
        this(address, null);
    }

    public ServerConnectionData(String address, String description) {
        this(address, description, null);
    }

    public ServerConnectionData(String address, String description, Date lastConnection) {
        this.address = address;
        this.description = description;
        this.lastConnection = lastConnection;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(Date lastConnection) {
        this.lastConnection = lastConnection;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isConnectionOpenable() {
        return address != null && !address.isEmpty() && !address.isBlank() && address.matches(IPV4_REGEX_VERIFIER);
    }

    @Override
    public boolean equals(Object obj) {
        boolean ris = false;

        if (obj instanceof ServerConnectionData) {
            ris = getAddress().equals(((ServerConnectionData)obj).getAddress());
            ris = ris && getAddress().equals(((ServerConnectionData)obj).getAddress());
            ris = ris && getAddress().equals(((ServerConnectionData)obj).getAddress());
        }
        else if (obj instanceof String) {
            ris = getAddress().equals(obj);
        }
        else {
            ris = super.equals(obj);
        }

        return ris;
    }
}
