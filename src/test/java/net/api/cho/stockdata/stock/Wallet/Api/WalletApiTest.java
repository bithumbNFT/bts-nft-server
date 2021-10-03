package net.api.cho.stockdata.stock.Wallet.Api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class WalletApiTest {


    @Test
    public void InttoHex(){
        String hex = "0x1bc16d674ec80000";
        double checkresult = hexCalculator(hex);
        System.out.println(hex.toLowerCase());
        System.out.println(checkresult);
        assertThat(checkresult,is(2.0));
    }

    double hexCalculator(String hex) {
        double result = 0;
        for (int i=0; i<=hex.length()-1;i++)
        {
            if(hex.charAt(i)>='A' && hex.charAt(i)<='F')
                result = result * 16 + hex.charAt(i) - 'A' + 10;
            else if(hex.charAt(i)>='a' && hex.charAt(i) <= 'f')
                result = result * 16 + hex.charAt(i) -'a' + 10;
            else if (hex.charAt(i) >='0' && hex.charAt(i) <= '9')
                result = result * 16 + hex.charAt(i) - '0';
        }
        double checkresult = result/Math.pow(10,18);
        return checkresult;
    }
}