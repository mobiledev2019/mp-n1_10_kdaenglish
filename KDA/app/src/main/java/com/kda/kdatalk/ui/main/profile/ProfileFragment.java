package com.kda.kdatalk.ui.main.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.ui.base.FragmentBase;
import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;
import com.kda.kdatalk.utils.MyGson;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends FragmentBase implements ProfileView {

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.iv_cover)
    ImageView iv_cover;

    @BindView(R.id.civ_profile)
    CircleImageView civ_profile;


    View viewRoot;
    private Context mContext;
    ProfilePresenter profilePresenter;
    User user;

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        profilePresenter = new ProfilePresenterImpl(mContext, this);
        if (!MyCache.getInstance().getString(DraffKey.user).isEmpty()) {
            user = MyGson.getInstance().fromJson(MyCache.getInstance().getString(DraffKey.user), User.class);
        }

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        if (user!= null) {
            Picasso.get().load(user.getUrl_img_ava())
                    .placeholder(R.drawable.user_no_img)
                    .error(R.drawable.user_no_img)
                    .into(civ_profile);

            tv_name.setText(user.name);


        }
    }

    @NonNull
    @Override
    protected int getContentView() {
        return R.layout.fragment_profile;
    }
}
