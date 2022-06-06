/*************************************************************************
 *  Compilation:  javac ArtCollage.java
 *  Execution:    java ArtCollage Flo2.jpeg
 *
 *  @author:
 *
 *************************************************************************/

import java.awt.Color;

public class ArtCollage {

    // The orginal picture
    private Picture original;

    // The collage picture
    private Picture collage;

    // The collage Picture consists of collageDimension X collageDimension tiles
    private int collageDimension;

    // A tile consists of tileDimension X tileDimension pixels
    private int tileDimension;
    
    /*
     * One-argument Constructor
     * 1. set default values of collageDimension to 4 and tileDimension to 100
     * 2. initializes original with the filename image
     * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension, 
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage (String filename) {
        this.collageDimension = 4;
        this.tileDimension = 100;
        this.original = new Picture(filename);
        this.collage = new Picture(this.tileDimension*this.collageDimension, this.tileDimension*this.collageDimension);
	    
        for (int col = 0; col < (this.tileDimension*this.collageDimension); col++){
            for (int row = 0; row < (this.tileDimension*this.collageDimension); row++){
                int scol = col*this.original.width()/(this.tileDimension*this.collageDimension);
                int srow = row*this.original.height()/(this.tileDimension*this.collageDimension);
                Color color = this.original.get(scol,srow);
                this.collage.set(col,row,color);
            }
        }
        // WRITE YOUR CODE HERE
    }

    /*
     * Three-arguments Constructor
     * 1. set default values of collageDimension to cd and tileDimension to td
     * 2. initializes original with the filename image
     * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension, 
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage (String filename, int td, int cd) {
        this.collageDimension = cd;
        this.tileDimension = td;
        this.original = new Picture(filename);
	    this.collage = new Picture(this.tileDimension*this.collageDimension, this.tileDimension*this.collageDimension);
	    
        for (int col = 0; col < (this.tileDimension*this.collageDimension); col++){
            for (int row = 0; row < (this.tileDimension*this.collageDimension); row++){
                int scol = col*this.original.width()/(this.tileDimension*this.collageDimension);
                int srow = row*this.original.height()/(this.tileDimension*this.collageDimension);
                Color color = this.original.get(scol,srow);
                this.collage.set(col,row,color);
            }
        }
        // WRITE YOUR CODE HERE
    }

    /*
     * Returns the collageDimension instance variable
     *
     * @return collageDimension
     */
    public int getCollageDimension() {
        return this.collageDimension;
	    // WRITE YOUR CODE HERE
    }

    /*
     * Returns the tileDimension instance variable
     *
     * @return tileDimension
     */
    public int getTileDimension() {
        return this.tileDimension;
	    // WRITE YOUR CODE HERE
    }

    /*
     * Returns original instance variable
     *
     * @return original
     */
    public Picture getOriginalPicture() {
        return this.original;
	    // WRITE YOUR CODE HERE
    }

    /*
     * Returns collage instance variable
     *
     * @return collage
     */
    public Picture getCollagePicture() {
        return this.collage;
	    // WRITE YOUR CODE HERE
    }
    
    /*
     * Display the original image
     * Assumes that original has been initialized
     */
    public void showOriginalPicture() {
        this.original.show();
	    // WRITE YOUR CODE HERE
    }

    /*
     * Display the collage image
     * Assumes that collage has been initialized
     */
    public void showCollagePicture() {
        this.collage.show();
	    // WRITE YOUR CODE HERE
    }

    /*
     * Replaces the tile at collageCol,collageRow with the image from filename
     * Tile (0,0) is the upper leftmost tile
     *
     * @param filename image to replace tile
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void replaceTile (String filename,  int collageCol, int collageRow) {
        int replaceColumn = collageCol*this.tileDimension;
        int replaceRow = collageRow*this.tileDimension;
        Picture temp = new Picture(filename);
        Picture scaled = new Picture(this.tileDimension, this.tileDimension);

        for (int col = 0; col<this.tileDimension; col++){
            for (int row = 0; row<this.tileDimension; row++){
                int scol = col*temp.width()/this.tileDimension;
                int srow = row*temp.height()/this.tileDimension;
                Color color = temp.get(scol,srow);
                scaled.set(col,row,color);
            }
        }
        for (int col = 0; col<this.tileDimension; col++){
            replaceRow = collageRow*this.tileDimension;
            for (int row = 0; row<this.tileDimension; row++){
                Color color = scaled.get(col, row);
                this.collage.set(replaceColumn, replaceRow, color);
                replaceRow++;
            }
            replaceColumn++;
        }
	    // WRITE YOUR CODE HERE
    }
    
    /*
     * Makes a collage of tiles from original Picture
     * original will have collageDimension x collageDimension tiles, each tile
     * has tileDimension X tileDimension pixels
     */
    public void makeCollage () {
        Picture scaled = new Picture(this.tileDimension,this.tileDimension);
        for (int col=0; col<this.tileDimension; col++){
            for (int row=0; row<this.tileDimension;row++){
                int scol = (col*this.original.width())/this.tileDimension;
                int srow = (row*this.original.height())/this.tileDimension;
                Color color = this.original.get(scol,srow);
                scaled.set(col,row,color);
            }
        }
        int fcol=0;
        for (int col=0; col<(this.tileDimension*this.collageDimension); col++){
            if (fcol == this.tileDimension){
                fcol = 0;
            }
            int frow = 0;
            for (int row = 0; row<(this.tileDimension*this.collageDimension);row++){
                if (frow == this.tileDimension){
                    frow = 0;
                }
                Color color = scaled.get(fcol,frow);
                this.collage.set(col,row,color);
                frow++;
            }
            fcol++;
        }
	    // WRITE YOUR CODE HERE
    }

    /*
     * Colorizes the tile at (collageCol, collageRow) with component 
     * (see CS111 Week 9 slides, the code for color separation is at the 
     *  book's website)
     *
     * @param component is either red, blue or green
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void colorizeTile (String component,  int collageCol, int collageRow) {
        int r=0;
        int g=0;
        int b=0;

        int replaceColumn = collageCol*this.tileDimension;
        int replaceRow = collageRow*this.tileDimension;

        for (int col = replaceColumn; col<(replaceColumn + this.tileDimension);col++){
            for (int row = replaceRow; col<(replaceRow + this.tileDimension);row++){
                Color color = this.collage.get(col,row);
                if (component == "red"){
                    r = color.getRed();
                }
                else if (component == "green"){
                    g = color.getGreen();
                }
                else if (component == "blue"){
                    b = color.getBlue();
                }
                this.collage.set(col,row,new Color(r,g,b));
            }
        }
	    // WRITE YOUR CODE HERE
    }

    /*
     * Grayscale tile at (collageCol, collageRow)
     * (see CS111 Week 9 slides, the code for luminance is at the book's website)
     *
     * @param collageCol tile column
     * @param collageRow tile row
     */

    public void grayscaleTile (int collageCol, int collageRow) {
        int replaceColumn = collageCol*this.tileDimension;
        int replaceRow = collageRow*this.tileDimension;
        for (int col = replaceColumn; col<(replaceColumn + this.tileDimension); col++){

            for (int row = replaceRow; row<(replaceRow + this.tileDimension); row++)
            {
                Color color = this.collage.get(col,row);
                Color gray = Luminance.toGray(color);
                this.collage.set(col, row, gray);
            }
        }
	    // WRITE YOUR CODE HERE
    }


    /*
     *
     *  Test client: use the examples given on the assignment description to test your ArtCollage
     */
    public static void main (String[] args) {

        ArtCollage art = new ArtCollage(args[0]); 
        art.showCollagePicture();
    }
}
