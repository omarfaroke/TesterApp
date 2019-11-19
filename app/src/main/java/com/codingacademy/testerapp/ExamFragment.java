package com.codingacademy.testerapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codingacademy.testerapp.model.Exam;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExamFragment extends Fragment {
RecyclerView recyclerExam;
List<Exam> examsArr;
ExamAdapter examAdapter;
    public ExamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_exam, container, false);
        fillExam();
        intViwes(v);
        return v;
    }
void fillExam(){
   examsArr=new ArrayList<>();
   examsArr.add(new Exam(null,null,"Java",null,null,null,null,null));
   examsArr.add(new Exam(null,null,"C#",null,null,null,null,null));
   examsArr.add(new Exam(null,null,"fluter",null,null,null,null,null));

}
    private void intViwes(View v) {
        recyclerExam=v.findViewById(R.id.recyclerExam);
        recyclerExam.setLayoutManager(new LinearLayoutManager( getActivity()));
recyclerExam.setAdapter(new ExamAdapter());
    }
private class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamVH> {

    @NonNull
    @Override
    public ExamVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExamVH(LayoutInflater.from(getActivity()).inflate(R.layout.temp_exam,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExamVH holder, int position) {
holder.examName.setText(examsArr.get(position).getExamName());
    }

    @Override
    public int getItemCount() {
        return examsArr.size();
    }

    private class ExamVH extends RecyclerView.ViewHolder{
        TextView examName;
            public ExamVH(@NonNull View itemView) {
                super(itemView);
                examName.findViewById(R.id.exam_name);
            }
        }
}
}
