package com.example.gamesquery.fragment;

/**
 * 机核网新闻界面Fragment
 */
public class JiheNewsFragment extends BaseWebFragment {
//
//    public static final String URI = "https://www.gcores.com/";
//    // 用来计算返回键的点击间隔时间
//    public WebSettings settings;
//    public static MyWebView wv_news;
//    public ProgressBar pb_load;
//    public SwipeRefreshLayout srl_news;
//    private Button btn_error;
//    public SharedPreferences preferences;
//    private LinearLayout ll_error;
//    private AnimationDrawable drawable;
//    private ImageView iv_loading;
//    private LinearLayout ll_loading;
//    public String hidehtml =
//            "javascript:function setHidehtml(){" +
//                    "document.getElementsByClassName('appDownloadTip')[0].style.display='none';" +
//                    "document.getElementsByClassName('gnav_container gnav_container-twoRow')[0].style.display='none';" +
//                    "}" +
//                    "setHidehtml();";
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        View view = inflater.inflate(R.layout.fragment_base, container, false);
////        wv_news = view.findViewById(R.id.wv_news);
//        pb_load = view.findViewById(R.id.pb_load);
//        srl_news = view.findViewById(R.id.srl_news);
//        ll_error = view.findViewById(R.id.ll_error);
//        btn_error = view.findViewById(R.id.btn_error);
//        iv_loading = view.findViewById(R.id.iv_loading);
//        drawable = (AnimationDrawable) iv_loading.getDrawable();
//        ll_loading = view.findViewById(R.id.ll_loading);
//        FragmentUtils.setWebView(wv_news, settings, getActivity(), hidehtml, pb_load, URI,ll_error, iv_loading, drawable, ll_loading);
//        FragmentUtils.setSwipeRefreshLayout(srl_news, wv_news,pb_load);
//        FragmentUtils.JudgStatus(getActivity(), settings, wv_news);
//        FragmentUtils.deleteWebCache(preferences, getActivity(), wv_news);
//        FragmentUtils.setErrorReload(btn_error, wv_news);
//        return view;
//    }
//
//    /**
//     * WebView后退判断
//     *
//     * @return
//     */
//    public static Boolean onBackPressed() {
//        if (wv_news.canGoBack()) {
//            wv_news.goBack();
//            return true;
//        }
//        return false;
//    }

    @Override
    public String getHidehtml() {
        return "javascript:function setHidehtml(){" +
                "document.getElementsByClassName('gnav_container gnav_container-twoRow')[0].style.display='none';" +
                "}" +
                "setHidehtml();";
    }


    @Override
    public String getUrl() {
        return "https://wap.gamersky.com";
    }


}