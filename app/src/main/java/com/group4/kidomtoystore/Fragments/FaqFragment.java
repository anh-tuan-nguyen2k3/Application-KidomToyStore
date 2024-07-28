package com.group4.kidomtoystore.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.group4.kidomtoystore.Adapters.FAQAdapter;
import com.group4.kidomtoystore.Models.FAQ;
import com.group4.kidomtoystore.R;
import com.group4.kidomtoystore.databinding.FragmentFaqBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaqFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaqFragment extends Fragment {

    FragmentFaqBinding binding;
    FAQAdapter adapter;
    ArrayList<FAQ> faqList;


    boolean isAnswerVisible = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FaqFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FaqFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FaqFragment newInstance(String param1, String param2) {
        FaqFragment fragment = new FaqFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFaqBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.item_faq, container, false);



        initData();
        loadData();


        return binding.getRoot();

    }


    private void initData() {
        faqList = new ArrayList<>();
        faqList.add(new FAQ(1, "Kidom kinh doanh gì?","Kidom là một cửa hàng kinh doanh đồ chơi trẻ em uy tín tại Hồ Chí Minh. Chúng tôi không chỉ cung cấp sản phẩm tại cửa hàng mà còn tiếp cận khách hàng thông qua Website và Ứng dụng Kidom.", FAQ.FAQCategory.GENERAL));
        faqList.add(new FAQ(2, "Tôi có thể thanh toán khi nhận sản phẩm không?","Tất nhiên. Bạn có thể lựa chọn phương thức thanh toán mà bạn cảm thấy thuận tiện nhất. Đương nhiên Kidom có cung cấp nhiều phương thức thanh toán từ trực tuyến đến trực tiếp để phục vụ khách hàng.", FAQ.FAQCategory.PAYMENT));
        faqList.add(new FAQ(3, "Làm thế nào để tôi có thể theo dõi đơn hàng của mình?","Kidom cung cấp chức năng theo dõi đơn hàng nhằm giúp khách hàng dễ dàng theo dõi trạng thái đơn hàng.", FAQ.FAQCategory.GENERAL));
    }

    private void loadData() {
        adapter = new FAQAdapter(getContext(),R.layout.item_faq,faqList);
        binding.lvFAQ.setAdapter(adapter);

    }

}