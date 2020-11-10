package edu.temple.project_post_it.test;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import static edu.temple.project_post_it.CONSTANT.GROUP_ID;
import static edu.temple.project_post_it.CONSTANT.LOCATION;
import static edu.temple.project_post_it.CONSTANT.POST_ID;
import static edu.temple.project_post_it.CONSTANT.PRIVACY;
import static edu.temple.project_post_it.CONSTANT.TEXT;
import static edu.temple.project_post_it.CONSTANT.TYPE;

public class test {
    String name;
    String color;
    String hobby;
    String age;
    String sex;

    public test() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", getName());
        result.put("color", getColor());
        result.put("hobby", getHobby());
        result.put("age", getAge());
        result.put("sex", getSex());
        return result;
    }
}
