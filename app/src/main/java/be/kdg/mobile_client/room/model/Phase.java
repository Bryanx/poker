package be.kdg.mobile_client.room.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

public enum Phase {

    @SerializedName("0")
    PRE_FLOP(0),

    @SerializedName("1")
    FLOP(1),

    @SerializedName("2")
    TURN(2),

    @SerializedName("3")
    RIVER(3),

    @SerializedName("4")
    SHOWDOWN(4);

    @Getter private int phase;

    Phase(int phase) {
        this.phase = phase;
    }
}