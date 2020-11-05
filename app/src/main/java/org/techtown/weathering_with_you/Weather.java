package org.techtown.weathering_with_you;

import java.util.ArrayList;

public class Weather {

    private ArrayList<Float> pop = new ArrayList<Float>();
    private ArrayList<Float> pty = new ArrayList<Float>();
    private ArrayList<Float> r06 = new ArrayList<Float>();
    private ArrayList<Float> reh = new ArrayList<Float>();
    private ArrayList<Float> s06 = new ArrayList<Float>();
    private ArrayList<Float> sky = new ArrayList<Float>();
    private ArrayList<Float> t3h = new ArrayList<Float>();
    private ArrayList<Float> tmn = new ArrayList<Float>();
    private ArrayList<Float> tmx = new ArrayList<Float>();
    private ArrayList<Float> uuu = new ArrayList<Float>();
    private ArrayList<Float> vec = new ArrayList<Float>();
    private ArrayList<Float> vvv = new ArrayList<Float>();
    private ArrayList<Float> wsd = new ArrayList<Float>();

    public ArrayList<Float> getPop() {
        return pop;
    }

    public void setPop(ArrayList<Float> pop) {
        this.pop = pop;
    }

    public ArrayList<Float> getPty() {
        return pty;
    }

    public void setPty(ArrayList<Float> pty) {
        this.pty = pty;
    }

    public ArrayList<Float> getR06() {
        return r06;
    }

    public void setR06(ArrayList<Float> r06) {
        this.r06 = r06;
    }

    public ArrayList<Float> getReh() {
        return reh;
    }

    public void setReh(ArrayList<Float> reh) {
        this.reh = reh;
    }

    public ArrayList<Float> getS06() {
        return s06;
    }

    public void setS06(ArrayList<Float> s06) {
        this.s06 = s06;
    }

    public ArrayList<Float> getSky() {
        return sky;
    }

    public void setSky(ArrayList<Float> sky) {
        this.sky = sky;
    }

    public ArrayList<Float> getT3h() {
        return t3h;
    }

    public void setT3h(ArrayList<Float> t3h) {
        this.t3h = t3h;
    }

    public ArrayList<Float> getTmn() {
        return tmn;
    }

    public void setTmn(ArrayList<Float> tmn) {
        this.tmn = tmn;
    }

    public ArrayList<Float> getTmx() {
        return tmx;
    }

    public void setTmx(ArrayList<Float> tmx) {
        this.tmx = tmx;
    }

    public ArrayList<Float> getUuu() {
        return uuu;
    }

    public void setUuu(ArrayList<Float> uuu) {
        this.uuu = uuu;
    }

    public ArrayList<Float> getVec() {
        return vec;
    }

    public void setVec(ArrayList<Float> vec) {
        this.vec = vec;
    }

    public ArrayList<Float> getVvv() {
        return vvv;
    }

    public void setVvv(ArrayList<Float> vvv) {
        this.vvv = vvv;
    }

    public ArrayList<Float> getWsd() {
        return wsd;
    }

    public void setWsd(ArrayList<Float> wsd) {
        this.wsd = wsd;
    }

    public void weatherInfo(){
        System.out.println("POP: "+pop);
        System.out.println("PTY"+pty);
        System.out.println("R06"+r06);
        System.out.println("REH"+reh);
        System.out.println("S06"+s06);
        System.out.println("SKY"+sky);
        System.out.println("T3H"+t3h);
        System.out.println("TMN"+tmn);
        System.out.println("TMX"+tmx);
        System.out.println("UUU"+uuu);
        System.out.println("VEC"+vec);
        System.out.println("VVV"+vvv);
        System.out.println("WSD"+wsd);
    }
}
