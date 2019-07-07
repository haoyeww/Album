package program.model;

import org.junit.jupiter.api.BeforeAll;
import java.util.ArrayList;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;


class DirectoryTest {
    private static Directory D;
    @BeforeAll
    static void beforeAll(){
        D = new Directory("phase2/unitTestImage");
    }

    @org.junit.jupiter.api.Test
    void testDirectory() {
        File A = new File("phase2/unitTestImage");
        assertEquals(A, D.getFile());
    }

    @org.junit.jupiter.api.Test
    void testImageLength() {
        int L = D.getImageFile().size();
        assertEquals(L,5);
    }

    @org.junit.jupiter.api.Test
    void testNameLength() {
        int L = D.getImageName().size();
        assertEquals(L,5);
    }

    @org.junit.jupiter.api.Test
    void testInitiateName() {
        ArrayList<String> A = new ArrayList<>();
        A.add("fox @yellow.jfif");
        A.add("lion @yellow.jfif");
        A.add("index.jpeg");
        A.add("aaa @cat.jpg");
        A.add("abc @bad.jpg");
        assertEquals(D.getImageName(),A);
    }


    @org.junit.jupiter.api.Test
    void testInitiateImage2(){
        File F = new File("phase2/unitTest/image1/image2/fox @yellow.jfif");
        ImageFile I = new ImageFile(F);
        assertTrue(I.equals(D.getImageFile().get(0)));
    }

    @org.junit.jupiter.api.Test
    void testInitiateImage1(){
        File F = new File("phase2/unitTest/image1/lion @yellow.jfif");
        ImageFile I = new ImageFile(F);
        assertTrue(I.equals(D.getImageFile().get(1)));
    }

    @org.junit.jupiter.api.Test
    void testSearchImage() {
        ImageFile I = D.searchImage("aaa @cat.jpg");
        assertEquals(I.getImageName(), "aaa @cat.jpg");
    }

    @org.junit.jupiter.api.Test
    void testSearchImage2() {
        ImageFile I = D.searchImage("lion @yellow.jfif");
        File F = new File ("phase2/unitTest/image1/lion @yellow.jfif");
        ImageFile I2 = new ImageFile(F);
        assertTrue(I.equals(I2));
    }

    @org.junit.jupiter.api.Test
    void testFindFormat() {
        ImageFile I = D.searchImage("aaa @cat.jpg");
        assertEquals(D.findFormat(I.getImage()), "jpg");
    }

    @org.junit.jupiter.api.Test
    void testFindFormat2() {
        ImageFile I = D.searchImage("lion @yellow.jfif");
        assertEquals(D.findFormat(I.getImage()), "jfif");
    }

}