package com.example.gdmap0fflinemap.activity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.gdmap0fflinemap.Buff.GetIntData;
import com.example.gdmap0fflinemap.Buff.PackageType.packageType;
import com.example.gdmap0fflinemap.data.AcceleratedSpeed;
import com.example.gdmap0fflinemap.data.GPSData;
import com.example.gdmap0fflinemap.data.GeomagnetismData;
import com.example.gdmap0fflinemap.data.IMUData;
import com.example.gdmap0fflinemap.data.MotorData;
import com.example.gdmap0fflinemap.data.RemoteControlData;
import com.example.gdmap0fflinemap.data.SpeedData;
import com.example.gdmap0fflinemap.data.StartCloseData;
import com.example.gdmap0fflinemap.data.StatusData;
import com.example.gdmap0fflinemap.data.WayPointData;
import com.example.gdmap0fflinemap.data.XBEEData;
import com.example.gdmap0fflinemap.eventBus.Accelerated_EventBus;
import com.example.gdmap0fflinemap.eventBus.GPS_EventBus;
import com.example.gdmap0fflinemap.eventBus.Geomagnetism_EventBus;
import com.example.gdmap0fflinemap.eventBus.IMU_EventBus;
import com.example.gdmap0fflinemap.eventBus.Motor_EventBus;
import com.example.gdmap0fflinemap.eventBus.Remote_EventBus;
import com.example.gdmap0fflinemap.eventBus.Speed_EventBus;
import com.example.gdmap0fflinemap.eventBus.StartClose_EventBus;
import com.example.gdmap0fflinemap.eventBus.Status_EventBus;
import com.example.gdmap0fflinemap.eventBus.WayPoint_EventBus;
import com.example.gdmap0fflinemap.eventBus.XBEE_EventBus;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;


/**
 * Created by Administrator on 2017/2/8.
 */

public class ClientThread implements Runnable {
    private Handler handler;
    static Socket socket;
    public Handler revHandle;
    private static final String TAG = "ClientThread";
    InputStream br = null;
    OutputStream os = null;

    byte[] Date = new byte[50];
    byte[] cIndexHeader = new byte[3];
    byte[] buffer = new byte[2048];
    byte[] ss = new byte[50];

    /**IMU����*/
    static String strRoll;//��ת��
    static String strPitch;//������
    static String strYaw;//�����
    static String strRollRate;//������Rollrate
    static String strPichRate;//������Pitchrate
    static String strYawRate;//������Yawrate

    /**���ٶ�����*/
    static String strAccX;//x������ٶ�
    static String strAccY;//y������ٶ�
    static String strAccZ;//z������ٶ�

    /**�ش�����*/
    static String strGeoX;
    static String strGeoY;
    static String strGeoZ;

    /**ң��������*/
    static String strReRoll;
    static String strRePitch;
    static String strReYaw;
    static String strReThrottle;

    /**��������*/
    static String strSW1;
    static String strSW2;
    static String strSW3;
    static String strSW4;

    /**·������*/
    static String strwayID;
    static String strwayCount;
    static String strwayIndex;

    /**�������*/
    static String strMotor_Front;
    static String strMotor_Back;
    static String strMotor_Left;
    static String strMotor_Right;
    static String strMotor_X;
    static String strMotor_Y;

    /**�ٶ�����*/
    static String strLateral;
    static String strLongitudinal;
    static String strAbout;

    /**GPS����*/
    static String strLatitude;
    static String strLongitude;
    static String strStarCount;
    static String strHight;

    /**״̬����*/
    static String strFlyStatus;
    static String strVoltage;
    static String strSendFlag;
    static String strSensorStatus;
    static String strCommStatus;
    static String strPhotoFlag;

    /**XBEEͨ�ϼ������*/
    static String strXBEE;

    //	������Ϣ
    packageType dataType;
    GetIntData getInt;
    /**IMU����*/
    IMUData IMU;
    /**���ٶ�����*/
    AcceleratedSpeed accspeed;
    /**�ش�����*/
    GeomagnetismData geoData;
    /**ң��������*/
    RemoteControlData remoteData;
    /**��������*/
    StartCloseData closeData;
    /**·������*/
    WayPointData wayPointData;
    /**�������*/
    MotorData motorData;
    /**�ٶ�����*/
    SpeedData speedData;
    /**GPS����*/
    GPSData gpsData;
    /**״̬����*/
    StatusData statusData;
    /**XBEEͨ�ϼ������*/
    XBEEData xbeeData;

//    public ClientThread(Handler handler) {
//        this.handler = handler;
//    }

    @Override
    public void run() {
        try {
            socket = new Socket(LinkIPActivity.sIP, LinkIPActivity.port);
            Log.d(TAG, "run: " + socket);
            br = new BufferedInputStream(socket.getInputStream());
            os = socket.getOutputStream();
            new Thread() {
                @Override
                public void run()
                {
                    int content ;
                    // ���϶�ȡSocket�������е�����
                    try
                    {
                        byte cRxDate;
                        int nRxDateCount = 0;
                        int nPacketSize = 0;
                        for (int y = 0; y < 3; y++) {
                            cIndexHeader[y] = 0;
                        }
                        boolean RxDataflag=false;
                        while(LinkIPActivity.isConnecting&&(br.read(buffer)!=-1))
                        {
                            content = br.read(buffer);
                            for(int i=0;i<content;i++)
                            {
                                cRxDate = buffer[i];
                                if(RxDataflag)
                                {
                                    Date[nRxDateCount++]=cRxDate;
                                    if(nRxDateCount>=nPacketSize){
                                        RxDataflag=false;
                                        //����õĸ�������ת������������ss����
                                        for (int x = 0; x <nPacketSize; x++)
                                        {
                                            if (Date[x] < 0)
                                            {
                                                ss[x] = (byte) (Date[x] + 256);
                                            } else
                                            {
                                                ss[x] = (Byte) Date[x];
                                            }
                                        }
                                        //����У��Ͳ��жϷ���������������Ƿ���ȷ
                                        int Bcc2 = 0;
                                        int Bcc = 0;
                                        for(int k=2;k<nPacketSize-2;k++)
                                        {
                                            Bcc+=((short)ss[k]&0x00ff);
                                        }
                                        Bcc2=(short)(((ss[nPacketSize-2]<<8)&0xFF00)|(ss[nPacketSize-1])&0x00FF);
                                        if(Bcc==Bcc2){
//										����date��2���Ĳ�ͬ���������
                                            switch (Date[2]) {
                                                case 1:
                                                    dataType = packageType.PtIMU;
                                                    IMU = new IMUData(ss, 3);
                                                    strRoll = String.valueOf(IMU.getIRoll());
                                                    strPitch = String.valueOf(IMU.getIPitch());
                                                    strYaw = String.valueOf(IMU.getIYaw());
                                                    strRollRate = String.valueOf(IMU.getIRollRate());
                                                    strPichRate = String.valueOf(IMU.getIPitchRate());
                                                    strYawRate = String.valueOf(IMU.getIYawRate());
                                                    Log.d(TAG, "run: "+strRoll);
                                                    EventBus.getDefault().post(new IMU_EventBus(strRoll,strPitch,strYaw,strRollRate,strPichRate,strYawRate));

//                                                    handler.sendEmptyMessage(1);
                                                    break;
                                                case 2:
                                                    dataType = packageType.PtAcceleratedSpeed;
                                                    accspeed = new AcceleratedSpeed(ss, 3);
                                                    strAccX = String.valueOf(accspeed.getX());
                                                    strAccY = String.valueOf(accspeed.getY());
                                                    strAccZ = String.valueOf(accspeed.getZ());
                                                    EventBus.getDefault().post(new Accelerated_EventBus(strAccX, strAccY, strAccZ));
//                                                    handler.sendEmptyMessage(2);
                                                    break;
                                                case 3:
                                                    dataType = packageType.PtGeomagnetism;
                                                    geoData = new GeomagnetismData(ss, 3);
                                                    strGeoX = String.valueOf(geoData.getX());
                                                    strGeoY = String.valueOf(geoData.getY());
                                                    strGeoZ = String.valueOf(geoData.getZ());
                                                    EventBus.getDefault().post(new Geomagnetism_EventBus(strGeoX,strGeoY,strGeoZ));
//                                                    handler.sendEmptyMessage(3);
                                                    break;
                                                case 4:
                                                    dataType = packageType.PtRemoteControl;
                                                    remoteData = new RemoteControlData(ss, 3);
                                                    strReRoll = String.valueOf(remoteData.getRoll());
                                                    strRePitch=String.valueOf(remoteData.getPitch());
                                                    strReYaw=String.valueOf(remoteData.getYaw());
                                                    strReThrottle=String.valueOf(remoteData.getThrottle());
//                                                    handler.sendEmptyMessage(4);
                                                    EventBus.getDefault().post(new Remote_EventBus(strReRoll,strRePitch,strReYaw,strReThrottle));
                                                    break;
                                                case 5:
                                                    dataType = packageType.PtStartClose;
                                                    closeData = new StartCloseData(ss, 3);
                                                    strSW1=String.valueOf(closeData.getSW1());
                                                    strSW2=String.valueOf(closeData.getSW2());
                                                    strSW3=String.valueOf(closeData.getSW3());
                                                    strSW4=String.valueOf(closeData.getSW4());
//                                                    handler.sendEmptyMessage(5);
                                                    EventBus.getDefault().post(new StartClose_EventBus(strSW1, strSW2, strSW3, strSW4));
                                                    break;
//
                                                case 6:
                                                    dataType = packageType.PtWayPoint;
                                                    wayPointData = new WayPointData(ss, 3);
                                                    strwayID=String.valueOf(wayPointData.getMotor_Front());
                                                    strwayCount=String.valueOf(wayPointData.getWayCount());
                                                    strwayIndex=String.valueOf(wayPointData.getWayIndex());
//                                                    handler.sendEmptyMessage(6);
                                                    EventBus.getDefault().post(new WayPoint_EventBus(strwayID, strwayCount, strwayIndex));

                                                    break;
//
                                                case 7:
                                                    dataType = packageType.PtMotor;
                                                    motorData = new MotorData(ss, 3);
                                                    strMotor_Front=String.valueOf(motorData.getMotor_Front());
                                                    strMotor_Back=String.valueOf(motorData.getMotor_Back());
                                                    strMotor_Left=String.valueOf(motorData.getMotor_Left());
                                                    strMotor_Right=String.valueOf(motorData.getMotor_Right());
                                                    strMotor_X=String.valueOf(motorData.getMotor_X());
                                                    strMotor_Y=String.valueOf(motorData.getMotor_Y());
//                                                    handler.sendEmptyMessage(7);
                                                    EventBus.getDefault().post(new Motor_EventBus(strMotor_Front, strMotor_Back, strMotor_Left, strMotor_Right, strMotor_X, strMotor_Y));

                                                    break;
//
                                                case 8:
                                                    dataType = packageType.PtSpeed;
                                                    speedData = new SpeedData(ss, 3);
                                                    strLateral=String.valueOf(speedData.getLateral());
                                                    strLongitudinal=String.valueOf(speedData.getLongitudinal());
                                                    strAbout=String.valueOf(speedData.getAbout());
//                                                    handler.sendEmptyMessage(8);
                                                    EventBus.getDefault().post(new Speed_EventBus(strLateral, strLongitudinal, strAbout));

                                                    break;
//
                                                case 9:
                                                    dataType = packageType.PtGPS;
                                                    gpsData = new GPSData(ss, 3);
                                                    strLatitude=String.valueOf(gpsData.getLatitude());
                                                    strLongitude=String.valueOf(gpsData.getLongitude());;
                                                    strStarCount=String.valueOf(gpsData.getStarCount());
                                                    strHight=String.valueOf(gpsData.getHight());
//                                                    handler.sendEmptyMessage(9);
                                                    EventBus.getDefault().post(new GPS_EventBus(strLatitude, strLongitude, strStarCount, strHight));
                                                    break;

                                                case 10:
                                                    dataType = packageType.PtStatus;
                                                    statusData = new StatusData(ss, 3);
                                                    strFlyStatus=String.valueOf(statusData.getFlyStatus());
                                                    strVoltage=String.valueOf(statusData.getVoltage());
                                                    strSendFlag=String.valueOf(statusData.getSendFlag());
                                                    strSensorStatus=String.valueOf(statusData.getSensorStatus());
                                                    strCommStatus=String.valueOf(statusData.getCommStatus());
                                                    strPhotoFlag=String.valueOf(statusData.getPhotoFlag());
//                                                    handler.sendEmptyMessage(10);
                                                    EventBus.getDefault().post(new Status_EventBus(strFlyStatus, strVoltage, strSendFlag, strSensorStatus, strCommStatus, strPhotoFlag));

                                                    break;

                                                case 11:
                                                    dataType = packageType.PtSpeed;
                                                    xbeeData = new XBEEData(ss, 3);
                                                    strXBEE=String.valueOf(xbeeData.getXBEE());
//                                                    handler.sendEmptyMessage(11);
                                                    EventBus.getDefault().post(new XBEE_EventBus(strXBEE));

                                                    break;
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    /**
                                     //									 * �Ȼ��ǰ3���ֽ�(2����ͷ1��ID),����Data��,�����cIndexHeader
                                     //									 * */
//									//����ͷ��3���ֽ�
                                    cIndexHeader[0] = cIndexHeader[1];
                                    cIndexHeader[1] = cIndexHeader[2];
                                    cIndexHeader[2] = cRxDate;
                                    //�жϰ�ͷ�Ƿ�����ȷ��
                                    if(cIndexHeader[0]==(byte)0xB5&&cIndexHeader[1]==(byte)0x5B)
                                    {
                                        for(int i1=0;i1<3;i1++)
                                        {
                                            Date[i1]=cIndexHeader[i1];
                                        }
                                        for(int i2=0;i2<3;i2++)
                                        {
                                            cIndexHeader[i2]=0;
                                        }
//									   ����ID����nPacketSize�Ĵ�С
                                        if (Date[2] < 1 && Date[2] > 11 )
                                        {
                                            Log.d(TAG, "run: 错误");
                                            break;
                                        }
                                        if (Date[2]==(byte)0x01)
                                        {
                                            nPacketSize=17;
                                        }
                                        else if (Date[2]==(byte)0x02)
                                        {
                                            nPacketSize=11;
                                        }
                                        else if (Date[2]==(byte)0x03)
                                        {
                                            nPacketSize=11;
                                        }
                                        else if (Date[2]==(byte)0x04)
                                        {
                                            nPacketSize=13;
                                        }
                                        else if (Date[2]==(byte)0x05)
                                        {
                                            nPacketSize=13;
                                        }
//                                        //��Ҫ��0x06�е�11��Ϊ19
                                        else if (Date[2]==(byte)0x06)
                                        {
                                            nPacketSize=11;
                                        }
                                        else if (Date[2]==(byte)0x07)
                                        {
                                            nPacketSize=17;
                                        }
                                        else if (Date[2]==(byte)0x08)
                                        {
                                            nPacketSize=11;
                                        }
                                        else if (Date[2]==(byte)0x09)
                                        {
                                            nPacketSize=25;
                                        }
                                        else if (Date[2]==(byte)0x0A)
                                        {
                                            nPacketSize=21;
                                        }
                                        else if (Date[2]==(byte)0x0B)
                                        {
                                            nPacketSize=7;
                                        }

                                        nRxDateCount=3;
                                        RxDataflag=true;
                                    }
                                }
                            }

                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }.start();
            Looper.prepare();
            revHandle = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 0x345) {
                        try {
                            os.write((msg.obj.toString() + "\r\n").getBytes("utf-8"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();
        } catch (SocketTimeoutException e1) {
            System.out.print("网络连接超时！！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
