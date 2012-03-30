package com.android.activity;

public class Contact {
String contact_name;
Long contact_id;
public Contact(String name) {
	
	this.contact_name=name;
}
public String getContact_name() {
	return contact_name;
}
public void setContact_name(String contact_name) {
	this.contact_name = contact_name;
}
public Long getContact_id() {
	return contact_id;
}
public void setContact_id(Long contact_id) {
	this.contact_id = contact_id;
}

}
