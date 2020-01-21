package com.candykick.huhs2ndmentoring;

import android.os.Bundle;

import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityMineSearchBinding;

public class MineSearchActivity extends BaseActivity<ActivityMineSearchBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);

        setNewGame();
    }

    //맨 처음 실행될 때, 다시하기 버튼 클릭 시 작동하는 함수
    //새 게임을 만들어준다.
    public void setNewGame() {

    }

    @Override
    protected int getLayoutId() { return R.layout.activity_mine_search; }
}
