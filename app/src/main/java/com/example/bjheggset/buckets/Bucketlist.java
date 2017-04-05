package com.example.bjheggset.buckets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KimRo on 04/04/2017.
 */

public class Bucketlist implements Serializable{
    public int id;
    public String bucketName;
    public long FK_fID;

    List<Items> itemsList = new ArrayList<>();

    public Bucketlist(int id, String bucketName, long FK_fID) {
        this.id = id;
        this.bucketName = bucketName;
        this.FK_fID = FK_fID;
    }

//    @Override
//    public String toString(){
//        return this.bucketName;
//    }

    public List<Items> getItems(){
        //TODO: Kjør sql spørring på relasjonsdatabasen etter denne Bucketlist sin ID.

        //TODO: Motta resultat fra DB, og legg alle items inn i list.

        return null; // Bare for å fjern feilmelding.
    }

}
