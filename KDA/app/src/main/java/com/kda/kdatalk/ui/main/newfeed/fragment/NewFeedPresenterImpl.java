package com.kda.kdatalk.ui.main.newfeed.fragment;

import android.content.Context;

import com.kda.kdatalk.R;
import com.kda.kdatalk.model.Comment;
import com.kda.kdatalk.model.NewFeed;
import com.kda.kdatalk.model.User;
import com.kda.kdatalk.network.APIUtils;
import com.kda.kdatalk.network.ServiceFunction;
import com.kda.kdatalk.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

public class NewFeedPresenterImpl implements NewFeedPresenter {
    private Context mContext;
    private NewFeedFragmentView newFeedFragmentView;

    ServiceFunction serviceFunction;

    private List<NewFeed> list_data;

    public NewFeedPresenterImpl(Context mContext, NewFeedFragmentView newFeedFragmentView) {
        this.mContext = mContext;
        this.newFeedFragmentView = newFeedFragmentView;
        list_data = new ArrayList<>();
        serviceFunction = APIUtils.getAPIService();
    }

    @Override
    public void setFragment(Fragment fragment) {
        if (fragment instanceof FragmentNewFeed) {
            newFeedFragmentView = (NewFeedFragmentView) fragment;
        }
    }

    @Override
    public void showProgress() {
        newFeedFragmentView.showProgress();
    }

    @Override
    public void hideProgress() {
        newFeedFragmentView.hideProgress();
    }

    @Override
    public List<NewFeed> getNewFeed() {
        // call Api

//        serviceFunction.getNewFeed("data","").enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                //
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                //
//            }
//        });

        showProgress();

        //
        String user_url = "https://scontent.fhan2-4.fna.fbcdn.net/v/t1.0-9/49467888_2205492239691741_6073977895720583168_n.jpg?_nc_cat=105&_nc_oc=AQlnAq4gT6wf3cNlgYB9IxfdPQVBwZR4vbVsmJilJ4gZ_7JAweGQnJAnzzuxiBzQyEA&_nc_ht=scontent.fhan2-4.fna&oh=da1c461ab945159660b34e6566c33504&oe=5D09D1E9";
        String user_url2 = "https://scontent.fhan2-4.fna.fbcdn.net/v/t1.0-9/56268549_343887426240652_7399425397634367488_n.jpg?_nc_cat=104&_nc_oc=AQlMvjFKX_eDQ6wVSdHzEbVadlDgYJVJRIZ-wralDDZPtwIjdcvOmEQAZOBrocKJB1mAgGMuLbiaJCaf_TApZwUc&_nc_ht=scontent.fhan2-4.fna&oh=253eab73429043cae685fcad7109d50b&oe=5D4B2FA1";


        String image_url = "https://scontent.fhan2-1.fna.fbcdn.net/v/t1.0-9/56158387_101611001027293_8255895292965027840_n.jpg?_nc_cat=103&_nc_oc=AQkASbXBNAb56hFfOpABdQyzK1vcm2S3s_4DytI5ZcJaZ0K3Y1y07ORUF97Fxw39nWD6y8-KxFWMz7F-iySWKddF&_nc_ht=scontent.fhan2-1.fna&oh=49defd2df95e8ba6687d5e17c2a6c50e&oe=5D420FB6";
        String image1 = "https://scontent.fhan2-3.fna.fbcdn.net/v/t1.0-9/26047171_1506922096094304_1165018699092051405_n.jpg?_nc_cat=109&_nc_oc=AQkKyOLhnfqK1xyu5WxF9_A1J3yblRHrbT2s9lrSaEKxPzsfw9ImTjXl5O_vW_UejklGpK0lkEcC2bap5C-ZQLeZ&_nc_ht=scontent.fhan2-3.fna&oh=57277c81b9232782d94ee79e20e02ec7&oe=5D424257";
        String image2 = "https://scontent.fhan2-3.fna.fbcdn.net/v/t1.0-9/56757361_632639193846341_200543250094751744_n.jpg?_nc_cat=109&_nc_oc=AQm7VIHJ2N5jUdurQKWQr8VBpdgBN5PzFhl3KjXo63cgBHjLT0MTlYdeCWqvfvEytI9mCqnAfgGSWWaUwdDYi-Mj&_nc_ht=scontent.fhan2-3.fna&oh=dde031e9ac9405880341abd3014b5af5&oe=5D3C15C4";
        String image3 = "https://scontent.fhan2-3.fna.fbcdn.net/v/t1.0-9/56842672_576861829475041_7200600430810759168_n.jpg?_nc_cat=108&_nc_oc=AQmuYDdOPvLGHO5w76p47DQqUjLmQ8DJeBlz1P9VTYr5A4kh3aiat8OM3Of_fMc8LPa5cysm7GcXHAKmCrsgftLA&_nc_ht=scontent.fhan2-3.fna&oh=5c2958d3ee7d34c517655c420d7a560d&oe=5D4C0984";
        String image4 = "https://scontent.fhan2-4.fna.fbcdn.net/v/t1.0-9/57267578_450296289043650_6587918222140375040_n.jpg?_nc_cat=104&_nc_oc=AQnmUrJtdE-7tzLuKbaz8OIsU5aCsDrxRFcCUQELLTsgnfp-OPHwT0FqSGxmrChZTeUJEySq4Qv-A4hPyUSH5cy0&_nc_ht=scontent.fhan2-4.fna&oh=bf7d2e24a4d122af0ee0e9506e635af0&oe=5D383A79";
        String image5 = "https://scontent.fhan2-4.fna.fbcdn.net/v/t1.0-9/57403848_688324381591192_4351470326673047552_n.jpg?_nc_cat=104&_nc_oc=AQmxx9e9lPd7WljKsWyOft8OWc8AdpPKYnTHjavBDXZ-L6d8AeyY6RaUV4vc1RVix9cqbbtsPhfOrz-ra1cOauZD&_nc_ht=scontent.fhan2-4.fna&oh=5eac18047fa21155e36b133e45d515d9&oe=5D481FE8";
        String image6 = "https://scontent.fhan2-4.fna.fbcdn.net/v/t1.0-9/56723930_787300708311416_7176898434744451072_n.jpg?_nc_cat=100&_nc_oc=AQnv8i87Sj4Z-5zweGkq6hHlDOQwbMYpTZralYZ_5-bynUJOomQz1LZJGXZYbdyPYdl89n7SKNpvAXFlBiNLe0Ef&_nc_ht=scontent.fhan2-4.fna&oh=945ade0f489333ef1fa14898af5ce789&oe=5D744059";
        //


        // fake
        String content = mContext.getResources().getString(R.string.lorem);

        ArrayList<String> image = new ArrayList<>();
        image.add(image1);
        image.add(image2);
        image.add(image3);
        image.add(image_url);
        image.add(image4);
        image.add(image2);
        image.add(image1);

        String htmt_str_err = "<span>There <span style=\"color:red\"><strike>is</strike></span> many chickens</span>";
        String htmt_str_corr = "<span>There <span style=\"color:#30FF00\">is</span> many chickens</span>";
//        String htmt_str_corr = "<p>Day la vi du ve the <span style=\"color:#30FF00\">the strike trong HTML</span></p>";

        User user = new User(1, "100", "", "vuanhlevis@gmail.com", "BN", "Vũ Anh", user_url, AppConstants.MODE_FACEBOOK);
        Comment comment = new Comment("1", "ahihi do ngok", htmt_str_err, htmt_str_corr, user, "32min");

        ArrayList<Comment> list_comment = new ArrayList<>();
        list_comment.add(comment);

        //
        htmt_str_err = "<span>What <span style=\"color:red\"><strike>do</strike></span> you do yesterday?</span>";
        htmt_str_corr = "<span>What <span style=\"color:#30FF00\">did</span> you do yesterday?</span>";
        comment = new Comment("2", "ban sai roi", htmt_str_err, htmt_str_corr, user, "1hr");
        //

        list_comment.add(comment);

        //
        htmt_str_err = "<span>What <span style=\"color:red\"><strike>do</strike></span> you do yesterday?</span>";
        htmt_str_corr = "<span>What <span style=\"color:#30FF00\">did</span> you do yesterday?</span>";
        comment = new Comment("3", "this is correct answer", htmt_str_err, htmt_str_corr, user, "3hr");
        //

        list_comment.add(comment);

        //
        htmt_str_err = "<span>What <span style=\"color:red\"><strike>do</strike></span> you do yesterday?</span>";
        htmt_str_corr = "<span>What <span style=\"color:#30FF00\">did</span> you do yesterday?</span>";
        comment = new Comment("2", "Too easy =))", htmt_str_err, htmt_str_corr, user, "8hr");
        //

        list_comment.add(comment);

        //String id, String content, String user_name, String user_url, List<String> list_image, String create_at, int num_like, int num_comment, boolean isLike
        list_data.add(new NewFeed("10000", content, "Vũ Cơ", user_url, image, "1 September 2018", 69, 17, true, list_comment));
        content = mContext.getResources().getString(R.string.newfeed1);
        image = new ArrayList<>();
        image.add(image6);

        list_data.add(new NewFeed("10001", content, "Sỹ Junior", image6, image, "17 October 2018", 10, 7, false, list_comment));
        content = mContext.getResources().getString(R.string.newfeed2);
        image = new ArrayList<>();
        image.add(image3);


        list_data.add(new NewFeed("10003", content, "Nguyễn Ngọc Huyền", image5, image, "29 January 2019", 102, 0, false, list_comment));
        content = mContext.getResources().getString(R.string.newfeed3);
        image = new ArrayList<>();
        image.add(image4);

        list_data.add(new NewFeed("10002", content, "Hương July", image1, image, "14 February 2019", 6, 1, false, list_comment));
        content = mContext.getResources().getString(R.string.newfeed4);
        image = new ArrayList<>();
        image.add(user_url2);

        list_data.add(new NewFeed("10004", content, "Lưu Mai Thúy", user_url2, image, "04 march 2019", 69, 5, true, list_comment));
        content = mContext.getResources().getString(R.string.newfeed5);
        image = new ArrayList<>();
        image.add(image5);

        list_data.add(new NewFeed("10005", content, "Mai Phạm", image4, image, "17 April 2019", 69, 5, true, list_comment));
        content = mContext.getResources().getString(R.string.newfeed6);
        image = new ArrayList<>();
        image.add(image1);

        list_data.add(new NewFeed("10006", content, "Nhung Kendy", image4, image, "17 April 2019", 69, 5, true, list_comment));
        content = mContext.getResources().getString(R.string.newfeed7);
        image = new ArrayList<>();
        image.add(image3);
        image.add(image4);

        list_data.add(new NewFeed("10007", content, "Nguyễn Trúc", image4, image, "17 April 2019", 69, 5, true, list_comment));
        content = mContext.getResources().getString(R.string.newfeed8);
        image = new ArrayList<>();
        image.add("https://scontent.fhan2-3.fna.fbcdn.net/v/t1.0-9/56679495_2267330376817662_7792169812909621248_n.jpg?_nc_cat=109&_nc_oc=AQmWNb1U54_xkIzlGm9_gDyAJ_CjYNnuqUYkKgXIAfn7YwWM-yW2_ZUmI1I9iBGOoEs4p-X3wob6OsW3ZmND3O16&_nc_ht=scontent.fhan2-3.fna&oh=a8e462a4f2dd35f83c3d3bad4f1d0c5c&oe=5D444BBC");

        list_data.add(new NewFeed("10008", content, "Nhung Kendy", image4, image, "17 April 2019", 69, 5, true, list_comment));


        hideProgress();
        return list_data;
    }
}
