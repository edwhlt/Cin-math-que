/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: NamedIdElement.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NamedIdElement extends IdElement {

    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName() + " [" + getId() + "]";
    }

}
