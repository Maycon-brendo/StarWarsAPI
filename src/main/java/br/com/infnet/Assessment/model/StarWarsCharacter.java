package br.com.infnet.Assessment.model;

import lombok.Data;

@Data
public class StarWarsCharacter {
    private String name;
    private String height;
    private String mass;
    private String hairColor;
    private String skinColor;
    private String eyeColor;
    private String birthYear;

    private String gender;

    public String getHair_color() {
        return hairColor;
    }
    public String getSkin_color() {
        return skinColor;
    }
    public String getEye_color() {
        return eyeColor;
    }
    public String getBirth_year() {
        return birthYear;
    }

    public void setHair_color(String hair_color) {
        this.hairColor = hair_color;
    }
    public void setSkin_color(String skin_color) {
        this.skinColor = skin_color;
    }
    public void setEye_color(String eye_color) {
        this.eyeColor = eye_color;
    }
    public void setBirth_year(String birth_year) {
        this.birthYear = birth_year;
    }
}
