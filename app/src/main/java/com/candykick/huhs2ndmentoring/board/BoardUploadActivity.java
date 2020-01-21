package com.candykick.huhs2ndmentoring.board;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityBoardUploadBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class BoardUploadActivity extends BaseActivity<ActivityBoardUploadBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);
    }

    //'등록하기' 버튼 클릭 시
    public void fnUpload() {
        if (binding.etUploadTitle.getText().toString().replace(" ", "").length() == 0) {
            binding.etUploadTitle.setText("");
            makeToastLong("제목을 입력해주세요.");
        } else if (binding.etUploadContents.getText().toString().replace(" ", "").length() == 0) {
            binding.etUploadContents.setText("");
            makeToastLong("내용을 입력해주세요.");
        } else if(binding.etUploadUsername.getText().toString().replace(" ", "").length() == 0) {
            binding.etUploadUsername.setText("");
            makeToastLong("유저이름을 입력해주세요.");
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> ideaData = new HashMap<>();
            ideaData.put("title", binding.etUploadTitle.getText().toString());
            ideaData.put("contents", binding.etUploadContents.getText().toString());
            ideaData.put("user", binding.etUploadUsername.getText().toString());

            db.collection("Board")
                    .document().set(ideaData, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void avoid) {
                            progressOff();
                            Intent intent = new Intent(BoardUploadActivity.this, BoardPostActivity.class);
                            intent.putExtra("user",ideaData.get("user").toString());
                            intent.putExtra("title",ideaData.get("title").toString());
                            intent.putExtra("contents",ideaData.get("contents").toString());
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressOff();
                            makeToastLong("글을 등록하는 도중 오류가 발생했습니다. 오류: " + e.toString());
                        }
                    });
        }
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_board_upload; }
}
