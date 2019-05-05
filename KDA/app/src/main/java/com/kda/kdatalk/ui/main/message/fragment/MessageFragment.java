package com.kda.kdatalk.ui.main.message.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.chat.MessageModel;
import com.kda.kdatalk.ui.base.FragmentBase;
import com.kda.kdatalk.ui.main.message.MessageActivity;
import com.kda.kdatalk.ui.main.message.fragment.adapter.Message_Adapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends FragmentBase implements MessageFragmentView {

    View viewRoot;

    Context mContext;

    @BindView(R.id.rv_chat)
    RecyclerView rv_chat;

    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.iv_ava)
    CircleImageView iv_ava;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    ArrayList<MessageModel> list_contact;
    Message_Adapter adapter;

    MessageFragmentPresenter presenter;
    Timer timer;


    public static MessageFragment newInstance() {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @NonNull
    @Override
    protected int getContentView() {
        return R.layout.fragment_message;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        try {
            ButterKnife.bind(viewRoot);
        } catch (Exception e) {

        }
        return viewRoot;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MessgaeFragmentImpl(mContext, this);
        list_contact = presenter.getListContact();

        getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get Data
        adapter = new Message_Adapter(mContext, list_contact, onItemClickListener);
        rv_chat.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(RecyclerView.VERTICAL);
//        manager.setStackFromEnd(true);
        rv_chat.setLayoutManager(manager);
        rv_chat.setNestedScrollingEnabled(false);
        rv_chat.setAdapter(adapter);

        setUpSearch();

    }

    private void setUpSearch() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                timer = new Timer();
                if (!s.toString().trim().isEmpty()) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // call api
                                    list_contact = presenter.getListContactBySearch(s.toString().trim());
                                    adapter.notifyDataSetChanged();
                                }
                            });

                        }
                    }, 1500);
                }
            }
        });
    }

    AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
        Intent intent = new Intent(mContext, MessageActivity.class);
        intent.putExtra("ID_MESS", list_contact.get(position).id);
        mContext.startActivity(intent);
    };

    @Override
    public void showProgress() {
        if (progress_bar != null) {
            progress_bar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (progress_bar != null) {
            progress_bar.setVisibility(View.GONE);
        }
    }
}
