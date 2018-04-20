/*
 * Copyright (C) 2018 Taktik SA
 *
 * This file is part of iCureBackend.
 *
 * iCureBackend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * iCureBackend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with iCureBackend.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.taktik.icure.be.drugs;
// Generated Feb 27, 2008 11:38:04 AM by Hibernate Tools 3.2.0.CR1



/**
 * Prop generated by hbm2java
 */
public class Prop  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
     private String key;
     private String value;

    public Prop() {
    }

	
    public Prop(String key) {
        this.key = key;
    }
    public Prop(String key, String value) {
       this.key = key;
       this.value = value;
    }
   
    public String getKey() {
        return this.key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }




}


