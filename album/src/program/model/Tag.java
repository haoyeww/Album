package program.model;

import java.util.ArrayList;

public class Tag {
    //the string representation for the tag
    private String tagName;
    //the ArrayList for all the ImageFile that has this tag
    private ArrayList<ImageFile> images = new ArrayList<>();

    /**
     * The constructor of the tag, it takes a String name and create a Tag object
     * careful: usually the Tag is constructed using the checkExists method in TagManager
     * so tags with same tagName is not created.
     * @param name the string one want to transform to tag(eg: cat)
     */
    Tag(String name){
        tagName = name;
    }


    /**
     * checkContain is a help method used to check if the images ArrayList
     * has a ImageFile that has a same imageName to the parameter.
     * @param I the ImageFile one wants to check
     * @return returns true if the images ArrayList contains this ImageFile
     * it will return false otherwise.
     */
    boolean checkContain(ImageFile I){
        for (ImageFile image: this.getImages()){
            if (image.getImageName().equals(I.getImageName())){
                return true;
            }
        }
        return false;
    }

    /**
     * the get method for TagName
     * @return return the TagName for this Tag
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * the get method for images ArrayList
     * @return return the images for this Tag
     */
    public ArrayList<ImageFile> getImages() {
        return images;
    }
}
