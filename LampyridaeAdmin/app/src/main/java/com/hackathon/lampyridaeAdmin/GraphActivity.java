package com.hackathon.lampyridaeAdmin;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.parseColor;

public class GraphActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    Spinner spinnerLocal, spinnerDong;
    Button btnSelect;

    ArrayList<lightInfo> lightArr;
    LinearLayout linearCountGraph;

    String[] items = {"전체","서울특별시","부산광역시","대구광역시","인천광역시","광주광역시","울산광역시",
    "세종시","경기도","강원도","충청북도","충청남도","전라북도","전라남도","경상북도","경상남도","제주도"};

    String[] items2 = {"내당1동","내당2·3동","내당4동","비산1동","비산2·3동","비산4동","비산7동",
    "상중이동","원대동","평리1동","평리2동","평리3동","평리4동","평리5동","평리6동"};

    String[] itemsAll = {"지역"};

    private GraphicalView mChartView;

    String location=null;

    Toolbar toolbar;

    ImageView ivBack;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==102) {
                lightArr = (ArrayList) msg.obj;
                drawChart();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        spinnerLocal = (Spinner)findViewById(R.id.spinnerLocal);
        spinnerDong = (Spinner)findViewById(R.id.spinnerDong);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 어댑터 설정
        spinnerLocal.setAdapter(adapter);

        spinnerLocal.setOnItemSelectedListener(this);

        //spinnerLocal.setBackgroundColor(Color.GRAY);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        btnSelect = (Button)findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(adapterView.getSelectedItem().equals("전체")){
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsAll);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 어댑터 설정
            spinnerDong.setAdapter(adapter2);
        }
        if(adapterView.getSelectedItem().equals("대구광역시")){
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items2);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 어댑터 설정
            spinnerDong.setAdapter(adapter2);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSelect :
                try{
                    location = spinnerDong.getSelectedItem().toString();

                    ConnectThread2 connectThread2 = new ConnectThread2(handler, location);

                    Log.e("TAG",location+", connectThread2 호출됨");
                    connectThread2.start();
                }catch (Exception e){

                }
                break;

            case R.id.ivBack :
                onBackPressed();
                break;
        }
    }

    private void drawChart() {
        String[] lightNums = new String[5];
        int[] count = new int[5];

        for(int i =0; i< lightArr.size() ;i++){
            //lightNums[i] = lightArr.get(i).getLightnum().substring(0,(location.indexOf(location)));
            String temp = lightArr.get(i).getLightnum();

            lightNums[i] = temp.substring(location.length(), temp.length());

            count[i] = lightArr.get(i).getCount();
        }

        int[] x = {0, 1, 2, 3, 4};

        //int[] colors = new int[]{Color.BLACK, Color.WHITE, Color.BLUE, Color.BLACK, Color.WHITE};

        // Creating an XYSeries for Income

        XYSeries incomeSeries = new XYSeries("빈도 수");

        // Creating an XYSeries for Expense

        // Adding data to Income and Expense Series

        for (int i = 0; i < x.length; i++) {

            incomeSeries.add(i, count[i]);
        }
        // Creating a dataset to hold each series

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        // Adding Income Series to the dataset

        dataset.addSeries(incomeSeries);

        // Creating XYSeriesRenderer to customize incomeSeries

        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();

        incomeRenderer.setColor(Color.parseColor("#421b56")); //color of the graph set to cyan

        incomeRenderer.setFillPoints(true);

        incomeRenderer.setLineWidth(2);

        incomeRenderer.setDisplayChartValues(true);

        incomeRenderer.setDisplayChartValuesDistance(10); //setting chart value distance

        incomeRenderer.setChartValuesTextSize(30);      // 차트 위 값

        incomeRenderer.setChartValuesTextAlign(Paint.Align.CENTER);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart

        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

        multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);

        multiRenderer.setXLabels(0);

        multiRenderer.setChartTitle(location+" 신고 빈도 수");

        multiRenderer.setXTitle("보안등 위치 명");

        multiRenderer.setXLabelsPadding(60);

        multiRenderer.setYTitle("신고 빈도 수");

        multiRenderer.setChartTitleTextSize(40);

        multiRenderer.setLabelsColor(Color.BLACK);

        //multiRenderer.setGridColor(Color.DKGRAY);


        multiRenderer.setXLabelsColor(BLACK);//x축 글자컬러

        multiRenderer.setYLabelsColor(0, BLACK);

        multiRenderer.setAxesColor(BLACK); //테두리라인컬러


        multiRenderer.setAxisTitleTextSize(30);

        //setting text size of the graph lable

        multiRenderer.setLabelsTextSize(30);



        //setting pan enablity which uses graph to move on both axis

        multiRenderer.setPanEnabled(false, false);

        //setting click false on graph

        multiRenderer.setClickEnabled(false);

        //setting zoom to false on both axis

        multiRenderer.setZoomEnabled(false, false);

        //setting lines to display on y axis

        multiRenderer.setShowGridY(true);

        //setting lines to display on x axis

        multiRenderer.setShowGridX(false);

        //setting legend to fit the screen size

        multiRenderer.setFitLegend(true);

        //setting displaying line on grid

        multiRenderer.setShowGrid(true);

        //setting zoom to false

        multiRenderer.setZoomEnabled(false);

        //setting external zoom functions to false

        multiRenderer.setExternalZoomEnabled(false);

        //setting displaying lines on graph to be formatted(like using graphics)

        multiRenderer.setAntialiasing(true);

        //setting to in scroll to false

        multiRenderer.setInScroll(false);

        //setting to set legend height of the graph

        multiRenderer.setShowLegend(false);

        multiRenderer.setXLabelsAlign(Paint.Align.CENTER);

        //setting y axis label to align

        multiRenderer.setYLabelsAlign(Paint.Align.LEFT);

        //setting text style

        multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);

        //setting no of values to display in y axis

        multiRenderer.setYLabels(10);   // Y축 값 간격

        // setting y axis max value, Since i'm using static values inside the graph so i'm setting y max value to 4000.

        // if you use dynamic values then get the max y value and set here
        multiRenderer.setYAxisMin(0);

        multiRenderer.setYAxisMax(count[0]*1.2);

        //setting used to move the graph on xaxiz to .5 to the right

        multiRenderer.setXAxisMin(-1);

        //setting max values to be display in x axis

        multiRenderer.setXAxisMax(5);

        //setting bar size or space between two bars

        multiRenderer.setBarSpacing(1);

        //Setting background color of the graph to transparent

        multiRenderer.setBackgroundColor(Color.TRANSPARENT);

        //Setting margin color of the graph to transparent

        multiRenderer.setMarginsColor(parseColor("#c6bdb2cf"));

        multiRenderer.setApplyBackgroundColor(true);

        //setting the margin size for the graph in the order top, left, bottom, right

        multiRenderer.setXLabelsPadding(10);

        multiRenderer.setYLabelsPadding(30);

        multiRenderer.setMargins(new int[]{50,50,150,50});

        for (int i = 0; i < x.length; i++) {

            multiRenderer.addXTextLabel(i, lightNums[i]);

        }
        // Adding incomeRenderer and expenseRenderer to multipleRenderer

        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer

        // should be same

        multiRenderer.addSeriesRenderer(incomeRenderer);

        //this part is used to display graph on the xml

        LinearLayout linearCountGraph = (LinearLayout) findViewById(R.id.countGraph);

        //remove any views before u paint the chart

        linearCountGraph.removeAllViews();

        //drawing bar chart

        mChartView = ChartFactory.getBarChartView(this, dataset, multiRenderer, BarChart.Type.DEFAULT);
        linearCountGraph .addView(mChartView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }
}