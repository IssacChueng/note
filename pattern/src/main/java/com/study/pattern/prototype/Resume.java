package com.study.pattern.prototype;

/**
 * 作为原型模式中的原型
 * @author swzhang
 * @date 2019/1/19
 * @description
 */
public class Resume implements Cloneable{
    private String name;

    private String birthiday;

    private String sex;

    private String school;

    private String timeArea;

    private String company;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    @Override
    public String toString() {
        return "Resume{" +
                "name='" + name + '\'' +
                ", birthiday='" + birthiday + '\'' +
                ", sex='" + sex + '\'' +
                ", school='" + school + '\'' +
                ", timeArea='" + timeArea + '\'' +
                ", company='" + company + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthiday() {
        return birthiday;
    }

    public void setBirthiday(String birthiday) {
        this.birthiday = birthiday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getTimeArea() {
        return timeArea;
    }

    public void setTimeArea(String timeArea) {
        this.timeArea = timeArea;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
