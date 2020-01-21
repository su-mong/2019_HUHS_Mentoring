package com.candykick.huhs2ndmentoring.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityCalculatorBinding;

public class CalculatorActivity extends BaseActivity<ActivityCalculatorBinding> {

    String strFormula = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);
    }

    public void btnCalculator0() {
        strFormula += "0";
        binding.etCalculator.setText(strFormula);
        binding.tvCalculator.setText("");
    }
    public void btnCalculator1() {
        strFormula += "1";
        binding.etCalculator.setText(strFormula);
        binding.tvCalculator.setText("");
    }
    public void btnCalculator2() {
        strFormula += "2";
        binding.etCalculator.setText(strFormula);
        binding.tvCalculator.setText("");
    }
    public void btnCalculator3() {
        strFormula += "3";
        binding.etCalculator.setText(strFormula);
        binding.tvCalculator.setText("");
    }
    public void btnCalculator4() {
        strFormula += "4";
        binding.etCalculator.setText(strFormula);
        binding.tvCalculator.setText("");
    }
    public void btnCalculator5() {
        strFormula += "5";
        binding.etCalculator.setText(strFormula);
        binding.tvCalculator.setText("");
    }
    public void btnCalculator6() {
        strFormula += "6";
        binding.etCalculator.setText(strFormula);
        binding.tvCalculator.setText("");
    }
    public void btnCalculator7() {
        strFormula += "7";
        binding.etCalculator.setText(strFormula);
        binding.tvCalculator.setText("");
    }
    public void btnCalculator8() {
        strFormula += "8";
        binding.etCalculator.setText(strFormula);
        binding.tvCalculator.setText("");
    }
    public void btnCalculator9() {
        strFormula += "9";
        binding.etCalculator.setText(strFormula);
        binding.tvCalculator.setText("");
    }

    public void btnCalculatorC() {
        strFormula = "";
        binding.etCalculator.setText(strFormula);
        binding.tvCalculator.setText("");
    }
    public void btnCalculatorback() {
        strFormula = strFormula.substring(0,strFormula.length()-1);
        binding.etCalculator.setText(strFormula);
    }

    public void btnCalculatorplus() {
        if(strFormula.charAt(strFormula.length()-1) != '+' & strFormula.charAt(strFormula.length()-1) != '-'
                & strFormula.charAt(strFormula.length()-1) != '×' & strFormula.charAt(strFormula.length()-1) != '÷'
                & strFormula.charAt(strFormula.length()-1) != '.') {
            strFormula += "+";
            binding.etCalculator.setText(strFormula);
            binding.tvCalculator.setText("");
        }
    }
    public void btnCalculatorminus() {
        if(strFormula.charAt(strFormula.length()-1) != '+' & strFormula.charAt(strFormula.length()-1) != '-'
                & strFormula.charAt(strFormula.length()-1) != '×' & strFormula.charAt(strFormula.length()-1) != '÷'
                & strFormula.charAt(strFormula.length()-1) != '.') {
            strFormula += "-";
            binding.etCalculator.setText(strFormula);
            binding.tvCalculator.setText("");
        }
    }
    public void btnCalculatormultiple() {
        if(strFormula.charAt(strFormula.length()-1) != '+' & strFormula.charAt(strFormula.length()-1) != '-'
                & strFormula.charAt(strFormula.length()-1) != '×' & strFormula.charAt(strFormula.length()-1) != '÷'
                & strFormula.charAt(strFormula.length()-1) != '.') {
            strFormula += "×";
            binding.etCalculator.setText(strFormula);
            binding.tvCalculator.setText("");
        }
    }
    public void btnCalculatordivision() {
        if(strFormula.charAt(strFormula.length()-1) != '+' & strFormula.charAt(strFormula.length()-1) != '-'
                & strFormula.charAt(strFormula.length()-1) != '×' & strFormula.charAt(strFormula.length()-1) != '÷'
                & strFormula.charAt(strFormula.length()-1) != '.') {
            strFormula += "÷";
            binding.etCalculator.setText(strFormula);
            binding.tvCalculator.setText("");
        }
    }
    public void btnCalculatordot() {
        if(strFormula.charAt(strFormula.length()-1) != '+' & strFormula.charAt(strFormula.length()-1) != '-'
                & strFormula.charAt(strFormula.length()-1) != '×' & strFormula.charAt(strFormula.length()-1) != '÷'
                & strFormula.charAt(strFormula.length()-1) != '.') {
            strFormula += ".";
            binding.etCalculator.setText(strFormula);
            binding.tvCalculator.setText("");
        }
    }
    public void btnCalculatormark() {

    }

    public void btnCalculatorresult() {
        binding.tvCalculator.setText(strFormula);
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_calculator; }
}
