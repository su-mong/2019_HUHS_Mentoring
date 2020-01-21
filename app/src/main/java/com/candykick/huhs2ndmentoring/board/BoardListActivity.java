package com.candykick.huhs2ndmentoring.board;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityBoardListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BoardListActivity extends BaseActivity<ActivityBoardListBinding> {

    ArrayList<BoardData> boardData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Board").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            BoardData data = new BoardData();
                            data.title = document.getData().get("title").toString();
                            data.contents = document.getData().get("contents").toString();
                            data.username = document.getData().get("user").toString();

                            boardData.add(data);
                        }

                        BoardListAdapter adapter = new BoardListAdapter(BoardListActivity.this, boardData);
                        binding.lvBoard.setAdapter(adapter);

                        binding.lvBoard.setOnItemClickListener((parentView, view, position, id) -> {
                            Intent intent = new Intent(BoardListActivity.this, BoardPostActivity.class);
                            intent.putExtra("title",boardData.get(position).title);
                            intent.putExtra("contents",boardData.get(position).contents);
                            intent.putExtra("user",boardData.get(position).username);
                            startActivity(intent);
                        });
                    } else {
                        makeToastLong("게시글이 없습니다.");
                    }
                } else {
                    makeToastLong("정보를 불러오는 도중 오류가 발생했습니다: "+task.getException());
                    finish();
                }
            }
        });
    }

    public void addPost() {
        Intent intent = new Intent(BoardListActivity.this, BoardUploadActivity.class);
        startActivity(intent);
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_board_list; }
}
