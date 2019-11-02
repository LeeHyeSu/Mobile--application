package lecture.mobile.final_project.ma01_20160989.model;

import java.io.Serializable;

public class MyPlace implements Serializable {      // intent의 putExtra 로 저장하기 위해 Serializable 구현
    private String type;
    private String name;
    private String phone;
    private String address;
    private String placeId;
    private double latitude;
    private double longitude;

    //    필수 정보로 객체를 생성하는 생성자
    public MyPlace(String name, String placeId, double latitude, double longitude) {
        this.name = name;
        this.placeId = placeId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return name + " (" + phone + ")";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

}