package smartcourier;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Random;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class SmartCourier extends JFrame {
    //menetapkan nomor untuk setiap warna yang akan digunakan
    int X = 1; //blok (kotak hitam)
    int C = 0; //ruang kosong (kotak putih)
    int S = 2; //keadaan awal
    int E = 8; //goal
    int V = 9; //jalan

    //keadaan awal (i,j)
    final static int START_I = 1, START_J = 1;
    //goal  (i,j)
    final static int END_I = 2, END_J = 9;

    int[][] smartcourier = new int[][]{ // format array awal untuk smartcourier
        {1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0},
        {1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 0, 0, 1, 1, 0, 1, 8, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0},
        {0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0},
        {0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0},
        {0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1},
        {0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1},
        {1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0},
        {0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1},
        {0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1},
        {1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
        {1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
        {0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0},
        {0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0},
        {0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
        {0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1}
    };

    int[][] arr;

    // Tombol Untuk GUI
    JButton solveStack;
    JButton clear;
    JButton randomMap;

    boolean repaint = false;

   // digunakan jika untuk menghapus (menghapus) solusi dari JFrame   
   int[][] savedSmartCourier = clone();

    // kelas smartcourier ini menjadi hal pertama yang dieksekusi
    public SmartCourier() {

        setTitle("City Map");     //Judul Untuk JFrame
        setSize(740, 530);    // Ukuran Untuk JFrame (lebar, tinggi)
        

        URL urlIcon = getClass().getResource("city-map.png");    // Path untuk gambar untuk The JFrame
        ImageIcon image = new ImageIcon(urlIcon);                // simpan gambar dalam variabel bernama gambar
        setIconImage(image.getImage());                          // atur Gambar Untuk JFrame

        setLocationRelativeTo(null);                                                
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        setLayout(null);                                       

        // inisialisasi objek untuk Buttons
        randomMap = new JButton("Random Map");
        clear = new JButton("Clear");
        solveStack = new JButton("Working ...");

        // Tambahkan Tombol ke JFrame
        add(solveStack);
        add(clear);
        add(randomMap);

        setVisible(true);

        // mengatur posisi komponen pada JFrame (x,y,width,height).       
        randomMap.setBounds(500, 100, 170, 40);
        clear.setBounds(500, 160, 170, 40);
        solveStack.setBounds(500, 220, 170, 40);

        // Generate Random SmartCourier
        randomMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int x[][] = GenerateArray();  
                repaint = true;
                restore(x);   
                repaint();   
            }
        });

        clear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (arr == null) {      
                    repaint = true;     
                    restore(savedSmartCourier); 
                    repaint();      
                } else {            
                    repaint = true; 
                    restore(arr);   
                    repaint();      
                }

            }
        });

        solveStack.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (arr == null) {     
                    restore(savedSmartCourier);  // kembalikan smartcourier ke aslinya
                    repaint = false;     
                    solveStack();        
                    repaint();           // ulangi smartcourier di JFrame
                } else {                 // terjadi jika array acak dihasilkan

                    restore(arr);        
                    repaint = false;     
                    solveStack();        
                    repaint();           // ulangi smartcourier di JFrame

                }

            }
        });

    }

    public int Size() {
        return smartcourier.length;
    }

    //mengembalikan nilai true jika sel berada di dalam smartcourier 
    public boolean isInSmartCourier(int i, int j) { 

        if (i >= 0 && i < Size() && j >= 0 && j < Size()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInSmartCourier(SMCourier pos) {  
        return isInSmartCourier(pos.i(), pos.j());  
    }

    // untuk menandai node dalam array dengan nilai tertentu;
    public int mark(int i, int j, int value) {
        assert (isInSmartCourier(i, j));  
        int temp = smartcourier[i][j];    
        smartcourier[i][j] = value;       
        return temp;              // kembalikan nilai asli
    }

    public int mark(SMCourier pos, int value) {   
        return mark(pos.i(), pos.j(), value);   
    }

    // mengembalikan true jika node sama dengan v=9 (Hijau, Dijelajahi)
    public boolean isMarked(int i, int j) {
        assert (isInSmartCourier(i, j));
        return (smartcourier[i][j] == V);

    }

    public boolean isMarked(SMCourier pos) {   
        return isMarked(pos.i(), pos.j());   
    }

    // mengembalikan true jika node sama dengan 0 (Putih, Belum Dijelajahi)
    public boolean isClear(int i, int j) {
        assert (isInSmartCourier(i, j));
        return (smartcourier[i][j] != X && smartcourier[i][j] != V);

    }

    public boolean isClear(SMCourier pos) {   
        return isClear(pos.i(), pos.j());   
    }

    
    public boolean isFinal(int i, int j) {

        return (i == SmartCourier.END_I && j == SmartCourier.END_J);
    }

    public boolean isFinal(SMCourier pos) {  
        return isFinal(pos.i(), pos.j());  
    }

    // buat Salinan dari smartcourier asli
    public int[][] clone() {
        int[][] smartcourierCopy = new int[Size()][Size()]; 
        for (int i = 0; i < Size(); i++) {
            for (int j = 0; j < Size(); j++) {
                smartcourierCopy[i][j] = smartcourier[i][j];
            }
        }
        return smartcourierCopy;
    }


    // untuk mengembalikan smartcourier ke keadaan awal
    public void restore(int[][] savedSmartCourierd) {
        for (int i = 0; i < Size(); i++) {
            for (int j = 0; j < Size(); j++) {
                smartcourier[i][j] = savedSmartCourierd[i][j];
            }
        }

        smartcourier[3][1] = 2;  // titik awal
        smartcourier[2][9] = 8;  // hasil
    }

    //menghasilkan smartcourier acak dengan nilai 0 dan 1 (blok hitam dan putih)
    public int[][] GenerateArray() {
        arr = new int[20][20];
        Random rndm = new Random();
        int min = 0;
        int high = 1;

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                int n = rndm.nextInt((high - min) + 1) + min;
                arr[i][j] = n;

            }
        }
          arr[0][1] = 0;arr[1][0] = 0;arr[2][1] = 0;arr[1][2] = 0;
          arr[1][9] = 0;arr[2][8] = 0;arr[3][9] = 0;              

        return arr;
    }

    //gambar map dan kurir di JFrame
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.translate(55, 70);  //pindahkan smartcourier mulai dari 70 dari x dan 70 dari y

        // menggambar kurir pintar
        if (repaint == true) {  
            for (int row = 0; row < smartcourier.length; row++) {
                for (int col = 0; col < smartcourier[0].length; col++) {
                    Color color;
                    switch (smartcourier[row][col]) {
                        case 1:
                            color = (new Color (98,52,18));     
                            break;
                        case 8:
                            color = Color.RED;        
                            break;
                        case 2:
                            color = Color.GREEN;      
                            break;
                        default:
                            color = Color.WHITE; 
                    }
                    g.setColor(color);
                    g.fillRect(20 * col, 20 * row, 20, 20);   
                    g.setColor(Color.BLACK);                  
                    g.drawRect(20 * col, 20 * row, 20, 20);   

                }
            }
        }

        if (repaint == false) {   
            for (int row = 0; row < smartcourier.length; row++) {
                for (int col = 0; col < smartcourier[0].length; col++) {
                    Color color = null;
                    switch (smartcourier[row][col]) {
                        case 1:
                            color = (new Color (98,52,18)); 
                            break;
                        case 8:
                            color = Color.RED;        
                            break;
                        case 2:
                            color = Color.GREEN;      
                            break;
                        case 9:
                            color = Color.BLUE;   
                            break;
                        default:
                            color = Color.WHITE;  
                    }
                    g.setColor(color);
                    g.fillRect(20 * col, 20 * row, 20, 20);  
                    g.setColor(Color.BLACK);                 
                    g.drawRect(20 * col, 20 * row, 20, 20);  

                }

            }

        }

    }

    public static void main(String[] args) {  // program utama

        SwingUtilities.invokeLater(new Runnable() {  // menjalankan program melalui Swing (seluruh program dijalankan oleh GUI)
                                                     
            @Override                             
            public void run() {
                SmartCourier smartcourier = new SmartCourier();      

            }
        });

    }

    public void solveStack() { //DFS sesuai dengan Stack

        Stack<SMCourier> stack = new Stack<SMCourier>();

        //masukkan simpul awal
        stack.push(new SMCourier(START_I, START_I));

        SMCourier crt;   //simpul saat ini
        SMCourier next;  //next node
        while (!stack.empty()) {//sementara tumpukan tidak kosong

            //dapatkan posisi saat ini dengan keluar dari tumpukan
            crt = stack.pop();
            if (isFinal(crt)) { //jika tujuan tercapai maka keluar, tidak perlu eksplorasi lebih lanjut.

                break;
            }

            //tandai posisi saat ini sebagai yang dijelajahi
            mark(crt, V);

            //dorong tetangganya di tumpukan
            next = crt.north();    // naik dari node saat ini
            if (isInSmartCourier(next) && isClear(next)) {  //isClear() metode digunakan untuk mengimplementasikan Graph Search (di pohon kita dapat menjelajahi ulang node, bisa terjebak dalam infinite loop yang terjadi pada build sebelumnya)
                stack.push(next);
            }
            next = crt.east();    //ke kanan dari node saat ini
            if (isInSmartCourier(next) && isClear(next)) {
                stack.push(next);
            }
            next = crt.west();    //ke kiri dari node saat ini
            if (isInSmartCourier(next) && isClear(next)) {
                stack.push(next);
            }
            next = crt.south();  // turun dari node saat ini
            if (isInSmartCourier(next) && isClear(next)) {
                stack.push(next);
            }
        }

        if (!stack.empty()) {          
            JOptionPane.showMessageDialog(rootPane, "Working ...");

        } else {                       
            JOptionPane.showMessageDialog(rootPane, "Solution not found");
        }

    }

}