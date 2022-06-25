package smartcourier;
public class SMCourier {

    int i,j;                   // cell position
    
    public SMCourier(int i, int j){
     this.i=i;
     this.j=j;
    };
    public int i() { return i;}   // get i (for overloaded methods)

    public int j() { return j;}   // get j (for overloaded methods)
    
    // go up
    public SMCourier north(){
      return new SMCourier(i-1,j);
    }
    
    //go down
    public SMCourier south(){
        return new SMCourier(i+1 , j);
    }
    
    //go right
    public SMCourier east(){
        return new SMCourier(i,j+1);
    }
    
    //go left
    public SMCourier west(){
      return new SMCourier(i,j-1);
    }
    
}
