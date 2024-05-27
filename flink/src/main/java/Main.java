/**
 * @author sunguiyong
 * @date 2024/5/23 18:30
 */
class Number {
    private char[] numArr;


    public char[] add(char[] num1, char[] num2) {
        //数组对齐,首位多设置一个0，加法进位
        if (num1.length > num2.length) {
            int diffLen = num1.length - num2.length + 1;
            num2 = setNewArr(num1, num2, diffLen);
        } else if (num1.length < num2.length) {
            int diffLen = num2.length - num1.length + 1;
            num1 = setNewArr(num1, num2, diffLen);
        }

        //计算
        int flag = 0;
        char[] res = new char[num1.length];
        for (int i = num2.length - 1; i >= 0; i--) {
            if (flag == 0) {
                if (num1[i] == '1' && num2[i] == '1') {
                    flag = 1;
                    res[i] = '0';
                } else {
                    res[i] = num1[i] == '1' || num2[i] == '1' ? '1' : '0';
                }
            } else {
                if (num1[i] == '1' && num2[i] == '1') {
                    flag = 1;
                    res[i] = '1';
                } else if (num1[i] == '1' || num2[2] == '1'){
                    flag = 1;
                    res[i] = '1';
                } else {
                    flag = 0;
                    res[i] = '1';
                }
            }

        }

        //res 去除首位0


        return null;
    }

    private char[] setNewArr(char[] num1, char[] num2, int diffLen) {

        for (int i = 0; i < diffLen; i++) {
            //对齐 前补0
            num2 = setByIndex(i, '0');
        }
        return num2;
    }

    public char[] setByIndex(int index, char value) {
        char[] tmp = new char[numArr.length + 1];
        int newIndex = 0;
        for (int i = 0; i < numArr.length; i++) {
            if (i == index) {
                tmp[newIndex] = value;
                newIndex++;
                tmp[newIndex] = numArr[i];
            } else {
                tmp[newIndex] = numArr[i];
            }
            newIndex++;
        }
        return tmp;
    }
}

public class Main {
    //数据结构 256bit 的整数
    //实现加减法
    public static void main(String[] args) {

    }
}
