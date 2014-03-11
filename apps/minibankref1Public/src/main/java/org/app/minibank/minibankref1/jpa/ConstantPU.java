package org.app.minibank.minibankref1.jpa;

public final class ConstantPU {

    // Value could be "minibank_derby" "minibank_oracle"
    // Used to inject the minibank persistence unit

    // property substitution do not work in annotation: @PersistenceContext(unitName = "${minibank.persistence.unit}")
    // And also do not work for annotation : System.getProperty("minibank.persistence.unit", "minibank_derby");

    // public final static String PU = "minibank_oracle";
    // public final static String PU = "minibank_derby";
    public final static String PU = "minibank_h2";

}
