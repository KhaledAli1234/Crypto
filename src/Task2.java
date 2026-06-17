import java.util.*;

public class Task2 {
    

    public String playfairEncrypt(String text, String key){
        char[][] m = matrix(key);
        text = prep(text);
        String r = "";
        for(int i=0;i<text.length();i+=2){
            int[] p = pos(m,text.charAt(i));
            int[] q = pos(m,text.charAt(i+1));
            if(p[0]==q[0])
                r += m[p[0]][(p[1]+1)%5]+""+m[q[0]][(q[1]+1)%5];
            else if(p[1]==q[1])
                r += m[(p[0]+1)%5][p[1]]+""+m[(q[0]+1)%5][q[1]];
            else
                r += m[p[0]][q[1]]+""+m[q[0]][p[1]];
        }
        return r.toUpperCase();
    }

    public String playfairDecrypt(String text, String key){
        char[][] m = matrix(key);
        text = text.toUpperCase();
        String r = "";
        for(int i=0;i<text.length();i+=2){
            int[] p = pos(m,text.charAt(i));
            int[] q = pos(m,text.charAt(i+1));
            if(p[0]==q[0])
                r += m[p[0]][(p[1]+4)%5]+""+m[q[0]][(q[1]+4)%5];
            else if(p[1]==q[1])
                r += m[(p[0]+4)%5][p[1]]+""+m[(q[0]+4)%5][q[1]];
            else
                r += m[p[0]][q[1]]+""+m[q[0]][p[1]];
        }
        return r.toUpperCase();
    }

    public String hillEncrypt(String text, String key){

        text = text.toUpperCase().replaceAll("[^A-Z]","");
        key  = key.toUpperCase().replaceAll("[^A-Z]","");

        int len = key.length();
        int n = (int)Math.sqrt(len);

        if(n*n != len)
            return "Key length must be perfect square (4,9,16,...)";

        int[][] matrix = new int[n][n];
        int index = 0;
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                matrix[i][j] = key.charAt(index++) - 'A';
            }
        }

        while(text.length() % n != 0)
            text += "X";

        StringBuilder result = new StringBuilder();

        for(int i=0;i<text.length();i+=n){

            for(int row=0; row<n; row++){

                int sum = 0;

                for(int col=0; col<n; col++){
                    int val = text.charAt(i+col) - 'A';
                    sum += matrix[row][col] * val;
                }

                result.append((char)((sum % 26) + 'A'));
            }
        }

        return result.toString();
    }

    public String hillDecrypt(String text, String key){

        text = text.toUpperCase().replaceAll("[^A-Z]","");
        key  = key.toUpperCase().replaceAll("[^A-Z]","");

        int len = key.length();
        int n = (int)Math.sqrt(len);

        if(n*n != len)
            return "Key length must be perfect square (4,9,16,...)";

        int[][] matrix = new int[n][n];
        int index = 0;
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++)
                matrix[i][j] = key.charAt(index++) - 'A';

        int[][] invMatrix = invertMatrix(matrix, n);

        if(invMatrix == null)
            return "Matrix not invertible mod 26";

        while(text.length() % n != 0)
            text += "X";

        StringBuilder result = new StringBuilder();

        for(int i=0;i<text.length();i+=n){

            for(int row=0; row<n; row++){

                int sum = 0;

                for(int col=0; col<n; col++){
                    int val = text.charAt(i+col) - 'A';
                    sum += invMatrix[row][col] * val;
                }

                result.append((char)((sum % 26 + 26) % 26 + 'A'));
            }
        }

        return result.toString();
    }

    public String vigenereEncrypt(String text, String key){
        text = text.toUpperCase().replaceAll("[^A-Z]","");
        key  = key.toUpperCase().replaceAll("[^A-Z]","");

        if(key.length()==0) return "Key required";

        StringBuilder r = new StringBuilder();

        for(int i=0;i<text.length();i++){
            int t = text.charAt(i)-'A';
            int k = key.charAt(i % key.length())-'A';
            r.append((char)(((t+k)%26)+'A'));
        }
        return r.toString();
    }

    public String vigenereDecrypt(String text, String key){
        text = text.toUpperCase().replaceAll("[^A-Z]","");
        key  = key.toUpperCase().replaceAll("[^A-Z]","");

        if(key.length()==0) return "Key required";

        StringBuilder r = new StringBuilder();

        for(int i=0;i<text.length();i++){
            int t = text.charAt(i)-'A';
            int k = key.charAt(i % key.length())-'A';
            r.append((char)(((t-k+26)%26)+'A'));
        }
        return r.toString();
    }

    public String oneTimePadEncrypt(String text, String key){
        text = text.toUpperCase().replaceAll("[^A-Z]","");
        key  = key.toUpperCase().replaceAll("[^A-Z]","");

        if(text.length()!=key.length())
            return "Key must be same length as text";

        StringBuilder r = new StringBuilder();

        for(int i=0;i<text.length();i++){
            int t = text.charAt(i)-'A';
            int k = key.charAt(i)-'A';
            r.append((char)(((t+k)%26)+'A'));
        }
        return r.toString();
    }

    public String railFenceEncrypt(String text, int rails){
        text = text.replaceAll("\\s+","");
        if(rails<=1) return text;

        StringBuilder[] fence = new StringBuilder[rails];
        for(int i=0;i<rails;i++) fence[i]=new StringBuilder();

        int row=0, dir=1;

        for(char c: text.toCharArray()){
            fence[row].append(c);
            row += dir;
            if(row==0 || row==rails-1) dir*=-1;
        }

        StringBuilder r = new StringBuilder();
        for(StringBuilder sb: fence) r.append(sb);

        return r.toString();
    }

    public String railFenceDecrypt(String text, int rails){
        if(rails<=1) return text;

        boolean[][] mark = new boolean[rails][text.length()];

        int row=0, dir=1;
        for(int col=0; col<text.length(); col++){
            mark[row][col]=true;
            row+=dir;
            if(row==0||row==rails-1) dir*=-1;
        }

        char[][] fence = new char[rails][text.length()];
        int index=0;

        for(int i=0;i<rails;i++)
            for(int j=0;j<text.length();j++)
                if(mark[i][j])
                    fence[i][j]=text.charAt(index++);

        StringBuilder r = new StringBuilder();
        row=0; dir=1;

        for(int col=0; col<text.length(); col++){
            r.append(fence[row][col]);
            row+=dir;
            if(row==0||row==rails-1) dir*=-1;
        }

        return r.toString();
    }

    public String rowTranspositionEncrypt(String text, String key){

        text = text.toUpperCase().replaceAll("[^A-Z]","");
        int cols = key.length();
        int rows = (int)Math.ceil((double)text.length()/cols);

        char[][] grid = new char[rows][cols];
        int index=0;

        for(int i=0;i<rows;i++)
            for(int j=0;j<cols;j++)
                grid[i][j] = (index<text.length()) ? text.charAt(index++) : 'X';

        Integer[] order = new Integer[cols];
        for(int i=0;i<cols;i++) order[i]=i;

        Arrays.sort(order,(a,b)->Character.compare(key.charAt(a),key.charAt(b)));

        StringBuilder r = new StringBuilder();

        for(int col: order)
            for(int row=0;row<rows;row++)
                r.append(grid[row][col]);

        return r.toString();
    }

    public String rowTranspositionDecrypt(String text, String key){

        int cols = key.length();
        int rows = text.length()/cols;

        char[][] grid = new char[rows][cols];

        Integer[] order = new Integer[cols];
        for(int i=0;i<cols;i++) order[i]=i;

        Arrays.sort(order,(a,b)->Character.compare(key.charAt(a),key.charAt(b)));

        int index=0;
        for(int col: order)
            for(int row=0;row<rows;row++)
                grid[row][col]=text.charAt(index++);

        StringBuilder r = new StringBuilder();

        for(int i=0;i<rows;i++)
            for(int j=0;j<cols;j++)
                r.append(grid[i][j]);

        return r.toString();
    }

    private int[][] invertMatrix(int[][] matrix, int n){

        int[][] aug = new int[n][2*n];

        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++)
                aug[i][j] = matrix[i][j];

            for(int j=n;j<2*n;j++)
                aug[i][j] = (i==(j-n)) ? 1 : 0;
        }

        for(int i=0;i<n;i++){

            int inv = inv(aug[i][i]);
            if(inv == -1) return null;

            for(int j=0;j<2*n;j++)
                aug[i][j] = (aug[i][j] * inv) % 26;

            for(int k=0;k<n;k++){
                if(k!=i){
                    int factor = aug[k][i];
                    for(int j=0;j<2*n;j++){
                        aug[k][j] = (aug[k][j] - factor*aug[i][j]) % 26;
                        if(aug[k][j] < 0) aug[k][j]+=26;
                    }
                }
            }
        }

        int[][] inv = new int[n][n];
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++)
                inv[i][j] = aug[i][j+n];

        return inv;
    }

    private int inv(int a){
        a = (a%26+26)%26;
        for(int i=1;i<26;i++)
            if((a*i)%26==1)
                return i;
        return -1;
    }
    private char[][] matrix(String key){
        key = key.toUpperCase().replace("J","I");
        LinkedHashSet<Character> set = new LinkedHashSet<>();
        for(char c:key.toCharArray())
            if(Character.isLetter(c)) set.add(c);
        for(char c='A'; c<='Z'; c++)
            if(c!='J') set.add(c);
        char[][] m = new char[5][5];
        Iterator<Character> it = set.iterator();
        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
                m[i][j] = it.next();
        return m;
    }

    private int[] pos(char[][] m, char c){
        if(c=='J') c='I';
        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
                if(m[i][j]==c) return new int[]{i,j};
        return null;
    }

    private String prep(String t){
        t = t.toUpperCase().replace("J","I").replaceAll("[^A-Z]","");
        StringBuilder s = new StringBuilder();
        for(int i=0;i<t.length();i++){
            char a = t.charAt(i);
            char b = (i+1<t.length()) ? t.charAt(i+1) : 'X';
            if(a==b){ s.append(a).append('X'); }
            else{ s.append(a).append(b); i++; }
        }
        if(s.length()%2!=0) s.append('X');
        return s.toString();
    }



}
