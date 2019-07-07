package program.model;

import java.util.ArrayList;

public class TagManager {
    //the ArrayList of Tags that collecting all the tag that checked in TagManager
    public static ArrayList<Tag> allTags = new ArrayList<>();

    /**
     * the checkExists is a method to check or create a Tag in the allTags List
     * it will check if the Tag with given name is already inside allTags List
     * if not, it will create a new Tag and add it into the allTag List
     * @param name the name of the Tag one wants to check or create
     * @return if the Tag already exists in allTags, it will just return that Tag
     * if the Tag does not exist, a new one will be created and added to allTags.
     */
    public static Tag checkExists (String name){
        for(Tag t : allTags){
            if(t.getTagName().equals(name)){
                return t;
            }
        }
        Tag T = new Tag(name);
        TagManager.addTag(T);
        return T;
    }

    /**
     * the deleteTag method to delete target Tag from allTags List
     * @param tag the Tag object one wants to remove
     */
    public static void deleteTag(Tag tag) {
        allTags.remove(tag);
    }

    /**
     * the addTag method to add target Tag into allTags List
     * @param tag the Tag object one wants to add
     */
    private static void addTag(Tag tag){
        allTags.add(tag);
    }

}

