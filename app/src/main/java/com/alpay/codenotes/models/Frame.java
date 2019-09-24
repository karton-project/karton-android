package com.alpay.codenotes.models;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class Frame{

    public static final String DRAWING_TYPE = "DRAWING";
    public static final String PHOTO_TYPE = "PHOTO";
    public static final String HEADER_TYPE = "HEADER";

    int frameID;
    int nextFrameID;
    int previousFrameID;
    String frameName;
    String frameGridType;
    String frameImageName;
    String frameGeneratedCode = "";

    public Frame() {

    }

    public Frame(int frameID, String frameName, String frameImageName, String frameGridType) {
        this.frameID = frameID;
        this.frameName = frameName;
        this.frameImageName = frameImageName;
        this.frameGridType = frameGridType;
    }

    public String getFrameGeneratedCode() {
        return frameGeneratedCode;
    }

    public void setFrameGeneratedCode(String frameGeneratedCode) {
        this.frameGeneratedCode = frameGeneratedCode;
    }

    public String getFrameImageName() {
        return frameImageName;
    }

    public int getFrameID() {
        return frameID;
    }

    public int getNextFrameID() {
        return nextFrameID;
    }

    public int getPreviousFrameID() {
        return previousFrameID;
    }

    public String getFrameGridType() {
        return frameGridType;
    }

    public String getFrameName() {
        return frameName;
    }

    public void setFrameImageName(String frameImageName) {
        this.frameImageName = frameImageName;
    }

    public void setFrameID(int frameID) {
        this.frameID = frameID;
    }

    public void setNextFrameID(int nextFrameID) {
        this.nextFrameID = nextFrameID;
    }

    public void setPreviousFrameID(int previousFrameID) {
        this.previousFrameID = previousFrameID;
    }

    public void setFrameGridType(String frameGridType) {
        this.frameGridType = frameGridType;
    }

    public void setFrameName(String frameName) {
        this.frameName = frameName;
    }


    public static void initEmptyStoryBoard(AppCompatActivity appCompatActivity) {
        Frame frame = new Frame(1, "Rafadan Tayfa", null, Frame.HEADER_TYPE);
        frame.save();
        frame = new Frame(2, "Hayrinin Sorunları", "frame1.png", Frame.PHOTO_TYPE);
        frame.save();
        frame = new Frame(3, "Kamile Ne Oldu?", "frame2.png", Frame.DRAWING_TYPE);
        frame.save();
        frame = new Frame(4, "Robotlar Hakkında", "frame3.png", Frame.DRAWING_TYPE);
        frame.save();
        frame = new Frame(4, "Şehrin Robotları", "frame4.png", Frame.DRAWING_TYPE);
        frame.save();
        frame = new Frame(1, "Kamil Robot mu?", null, Frame.HEADER_TYPE);
        frame.save();
        frame = new Frame(1, "Rafadan İş Başında", "frame5.png", Frame.PHOTO_TYPE);
        frame.save();
    }

    public static void updateFrameImageName(int frameID, String imageName) {
        Frame frame = Frame.findById(frameID);
        frame.setFrameImageName(imageName);
        frame.save();
    }

    public static void updateFrameTitle(int frameID, String title) {
        Frame frame = Frame.findById(frameID);
        frame.setFrameName(title);
        frame.save();
    }

    public static int getLastID() {
        return Frame.count();
    }

    public static void addNewEmptyFrame() {
        Frame frame = new Frame(getLastID() + 1, "Frame Name", null, Frame.PHOTO_TYPE);
        frame.save();
    }

    public static void addNewEmptyHeader() {
        Frame frame = new Frame(getLastID() + 1, "Chapter Name", null, Frame.HEADER_TYPE);
        frame.save();
    }

    public static void updateFrameCode(int frameID, String generetedCode){
        Frame frame = Frame.findById(frameID);
        frame.setFrameGeneratedCode(generetedCode);
        frame.save();
    }

    public static int count(){
        return FrameHelper.getFrameListSize();
    }

    public static Frame findById(int id){
        return FrameHelper.frameList.get(id);
    }

    public void save(){
        FrameHelper.frameList.add(this);
    }

    public static List<Frame> listAll(){
        return FrameHelper.frameList;
    }

}
