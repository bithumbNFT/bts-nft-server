package net.api.cho.stockdata.stock.Wallet.Api;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class SendKlayTest {

    @Test
    void changeNumber() {
        String hex = changeNumber(2*Math.pow(10,18));
        assertThat(hex.toLowerCase(),is(equalTo("0x1bc16d674ec80000")));
    }
    public static String changeNumber(double num) {
        String answer = "";
        int remainder = 0;
        while(num != 0) {
            remainder = (int) (num%16);
            if(num%16 < 10) {
                answer = remainder + answer;
            } else {
                answer = (char)(remainder + 55) + answer;
            }
            num /= 16;
        }
        String resultdata = "";
        for (int i = 0; i<answer.length()-1;i++){
            if(answer.charAt(i) == '0'){
                continue;
            }
            else {
                System.out.println(i);
                resultdata = answer.substring(i,answer.length());
                break;
            }
        }
        System.out.println("return data =" + resultdata);
        return "0x"+resultdata;
    }
}