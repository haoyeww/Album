package program.model;

import org.junit.jupiter.api.BeforeAll;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
class ImageFileTest {
    private static File f;
    private static ImageFile i;
    private static Tag t;
    private static Tag t2;
    @BeforeAll
    static void beforeAll(){
        Directory d = new Directory("phase2/unitTestImage");
        i = d.getImageFile().get(3);
        f = new File("phase2/unitTestImage/aaa @cat.jpg");
        t = TagManager.checkExists("cute");
        t2 = TagManager.checkExists("cat");

    }

    @org.junit.jupiter.api.Test
    void testImageName(){
        assertEquals(i.getImageName(), f.getName());
    }

    @org.junit.jupiter.api.Test
    void testImageLocation(){
        assertEquals(i.getLocation(), f.getPath());
    }

    @org.junit.jupiter.api.Test
    void testOriginalName(){
        assertEquals(i.getOriginalName(), "aaa.jpg");
    }

    @org.junit.jupiter.api.Test
    void testAddNewTag(){
        System.out.println(i.getTagNames());
        new ImageEditor(i).addNewTag(t);
        assertEquals(i.getImageName(), "aaa @cat @cute.jpg");
        // assuming delete is working and set the tag back
        new ImageEditor(i).deleteTag(t);
    }

    @org.junit.jupiter.api.Test
    void testDeleteTag(){
        System.out.println(i.getTags());
        new ImageEditor(i).deleteTag(t2);
        assertEquals(i.getImageName(), "aaa.jpg");
        // assuming add is working and revert the tag
        new ImageEditor(i).addNewTag(t2);
    }

    @org.junit.jupiter.api.Test
    void testGoBack(){
        new ImageEditor(i).addNewTag(t);
        System.out.println(Log.getInstance().getLogs(i));
        int index = Log.getInstance().getLogs(i).size() - 1;
        String time = Log.getInstance().getLogs(i).get(index).get(2);
        new ImageEditor(i).goBack(time);
        assertEquals(i.getImageName(), "aaa @cat.jpg");
    }

}
