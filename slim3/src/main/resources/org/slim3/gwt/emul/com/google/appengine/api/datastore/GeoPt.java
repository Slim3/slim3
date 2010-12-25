package com.google.appengine.api.datastore;

import java.io.Serializable;

public final class GeoPt implements Serializable {

    public static final long serialVersionUID = -1204620895L;
    private float latitude;
    private float longitude;

    public GeoPt(float latitude, float longitude) {
        if (Math.abs(latitude) > 90F)
            throw new IllegalArgumentException(
                "Latitude must be between -90 and 90 (inclusive).");
        if (Math.abs(longitude) > 180F) {
            throw new IllegalArgumentException(
                "Latitude must be between -180 and 180.");
        } else {
            this.latitude = latitude;
            this.longitude = longitude;
            return;
        }
    }

    private GeoPt() {
        this(0.0F, 0.0F);
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public int compareTo(GeoPt o) {
        int latResult =
            Float.valueOf(latitude).compareTo(Float.valueOf(o.latitude));
        if (latResult != 0)
            return latResult;
        else
            return Float.valueOf(longitude).compareTo(
                Float.valueOf(o.longitude));
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GeoPt geoPt = (GeoPt) o;
        if (Float.compare(geoPt.latitude, latitude) != 0)
            return false;
        return Float.compare(geoPt.longitude, longitude) == 0;
    }

    public int hashCode() {
        int result = latitude == 0.0F ? 0 : floatToIntBits(latitude);
        result =
            31 * result + (longitude == 0.0F ? 0 : floatToIntBits(longitude));
        return result;
    }

    public String toString() {
        return Float.valueOf(latitude) + "," + Float.valueOf(longitude);
    }

    private static final double LN2 = Math.log(2);

    // Theory of operation: Let a double number d be represented as
    // 1.M * 2^E, where the leading bit is assumed to be 1,
    // the fractional mantissa M is multiplied 2 to the power of E.
    // We want to reliably recover M and E, and then encode them according
    // to IEEE754 (see http://en.wikipedia.org/wiki/IEEE754)
    static int floatToIntBits(float value) {
        if (Float.isNaN(value)) {
            return 0x7fc00000;
        }
        int signBit;
        if (value == 0) {
            return (1 / value == Float.NEGATIVE_INFINITY) ? 0x80000000 : 0;
        } else if (value < 0) {
            value = -value;
            signBit = 0x80000000;
        } else {
            signBit = 0;
        }
        if (value == Float.POSITIVE_INFINITY) {
            return signBit | 0x7f800000;
        }

        int exponent = (int) (Math.log(value) / LN2);
        if (exponent < -126) {
            exponent = -126;
        }
        int significand =
            (int) (0.5 + value * Math.exp(-(exponent - 23) * LN2));

        // Handle exponent rounding issues & denorm
        if ((significand & 0x01000000) != 0) {
            significand >>= 1;
            exponent++;
        } else if ((significand & 0x00800000) == 0) {
            if (exponent == -126) {
                return signBit | significand;
            } else {
                significand <<= 1;
                exponent--;
            }
        }
        return signBit | ((exponent + 127) << 23) | (significand & 0x007fffff);
    }

    static String toBinaryIeee754String(long decimal) {
        String binary = Long.toBinaryString(decimal);
        StringBuilder result = new StringBuilder(binary);
        for (long i = binary.length(); i < 32; i++) {
            result.insert(0, "0");
        }
        result.insert(9, " ");
        result.insert(1, " ");
        return result.toString();
    }
}