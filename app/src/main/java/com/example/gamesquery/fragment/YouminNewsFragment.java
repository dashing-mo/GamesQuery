package com.example.gamesquery.fragment;

/**
 * 游民星空新闻界面Fragment
 */
public class YouminNewsFragment extends BaseWebFragment {

    @Override
    public String getHidehtml() {
        return "javascript:function setHidehtml(){" +
                "document.querySelector('.ymw-header2018').style.display='none';" +
                "document.querySelector('.ymw-footer').style.display='none';" +
                "document.querySelector('.yu-btn-wrap').style.display='none';" +
                "document.querySelector('.gsTgWapZPCbtn countHit countHitSql').style.display='none';" +
                "document.querySelector('.ymw-search-res-nav ymw-search-res-nav2018').style.display='none';" +
                "document.getElementsByClassName('appDownloadTip')[0].style.display='none';" +
                "document.getElementById('gsTgWapConBdshareTop').style.display='none';" +
                "document.getElementsByClassName('ymw-rel-list')[0].style.display='none';" +
                "document.getElementsByClassName('ymw-rel-mgame')[0].style.display='none';" +
                "}" +
                "setHidehtml();";
    }



    @Override
    public String getUrl() {
        return "https://wap.gamersky.com";
    }

    @Override
    public Boolean setWebClose() {
        return false;
    }

    @Override
    public Boolean setApplicationWebClose() {
        return true;
    }

    //    public static final String URI = "https://wap.gamersky.com";
//    // 用来计算返回键的点击间隔时间
//    public WebSettings settings;
//    public LinearLayout ll_youmin;
//    public ProgressBar pb_load;
//    public SwipeRefreshLayout srl_news;
//    private LinearLayout ll_error;
//    private Button btn_error;
//    public SharedPreferences preferences;
//    private AnimationDrawable drawable;
//    private ImageView iv_loading;
//    private WindowManager manager;
//    private View view1;
//    private LinearLayout ll_loading;
//    public String hidehtml =
//            "javascript:function setHidehtml(){" +
//                    "document.querySelector('.ymw-header2018').style.display='none';" +
//                    "document.querySelector('.ymw-footer').style.display='none';" +
//                    "document.querySelector('.yu-btn-wrap').style.display='none';" +
//                    "document.querySelector('.gsTgWapZPCbtn countHit countHitSql').style.display='none';" +
//                    "document.querySelector('.ymw-search-res-nav ymw-search-res-nav2018').style.display='none';" +
//                    "document.getElementsByTagName('body')[0].innerHTML;" +
//                    "document.getElementsByTagName('div')[7].style.display='none';" +
//                    "document.getElementsByClassName('appDownloadTip')[0].style.display='none';" +
//                    "document.getElementById('gsTgWapZPCbtn').style.display='none';" +
//                    "document.getElementById('gsTgWapConBdshareTop').style.display='none';" +
//                    "document.getElementsByClassName('ymw-rel-list')[0].style.display='none';" +
//                    "document.getElementsByClassName('ymw-rel-mgame')[0].style.display='none';" +
//                    "}" +
//                    "setHidehtml();";
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        View view = inflater.inflate(R.layout.fragment_news_youmin, container, false);
//        manager = getActivity().getWindowManager();
//        ll_youmin = view.findViewById(R.id.ll_youmin);
//        pb_load = view.findViewById(R.id.pb_load);
//        srl_news = view.findViewById(R.id.srl_news);
//        ll_error = view.findViewById(R.id.ll_error);
//        btn_error = view.findViewById(R.id.btn_error);
//        iv_loading = view.findViewById(R.id.iv_loading);
//        drawable = (AnimationDrawable) iv_loading.getDrawable();
//        ll_loading = view.findViewById(R.id.ll_loading);
//        BaseApplication application = (BaseApplication) getActivity().getApplication();
//        ll_youmin.addView(application.wv1);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        application.wv1.setLayoutParams(params);
//        FragmentUtils.setHomeWebView(application.wv1, settings, getActivity(), hidehtml, pb_load, ll_error, iv_loading, drawable, ll_loading, manager, view1);
//        FragmentUtils.setSwipeRefreshLayout(srl_news, application.wv1, pb_load);
//        FragmentUtils.JudgStatus(getActivity(), settings, application.wv1);
//        FragmentUtils.deleteWebCache(preferences, getActivity(), application.wv1);
//        FragmentUtils.setErrorReload(btn_error, application.wv1);
////        application.wv1.setOnKeyListener(new ExitOnKeyListener(application.wv1, getActivity(), view1, manager));
//        return view;
//    }

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

}