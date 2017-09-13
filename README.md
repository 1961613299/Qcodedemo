# 米付二维码接口

## 1.1接口标准

1、  通讯协议类型为http

2、  报文格式为json格式

3、  编码格式采用UTF-8

4、  请求使用POST数据流方式提交请求, 字段值“data”，value为加密后的字段

5、  请求url：

商户报备接口： http://payapp.midaipay.com/channel/auth
签到接口： http://payapp.midaipay.com/channel/signForMacKey
二维码请求接口： http://payapp.midaipay.com/channel/getQRCode
二维码交易查询接口： http://payapp.midaipay.com/channel/queryStatus


## 1.2 加密规则

具体数据传输采用三层加密模式（Base64+DES+UrlEncode），主JSON数据串用MD5进行前后验证（即发送前准备密文，接收后进行三重解密+MD5加密验证，防止数据被篡改）
，返回数据只用UrlEncode加解密


## 1.3 接口详情

### 1.3.1商户报备接口

#### 传入参数
加密后请求样式（XXX为加密后json数据）：    {“data”:"XXXXX"}


|        字段名        |       字段描述       |               备注               |
| :---------------: | :--------------: | :----------------------------: |
|       type        |   固定值：insData    |             报件固定值              |
|     instCode      |      所属收单机构      |            分配的代理商编号            |
|       mcc32       |     收单机构32域      |            附件32域文档             |
|  traderAreaCode   |     商户号地区编码      | 商户号地区编码请自行在网上查找《中华人民共和国行政区划代码》 |
|       mcc42       |      42域MCC      |   附件汇金提供-银联特约商户手续费率一览表中mcc的值   |
|  busLicenseName   |     商户工商注册名称     |                                |
|      busName      |       营业名称       |                                |
|    busLicense     |      营业执照号码      |         个人商户次字段可填写身份证          |
|     busScope      |       经营范围       |                                |
|   mainbusiness    |       主营业务       |                                |
|     legalName     |       法人代表       |                                |
|      cerType      |   法人证件类型，默认01    |          固定值，仅支持身份证号           |
|      cerNum       |     法人代表身份证号     |                                |
|    contactName    |      法人代表名称      |                                |
|   contactPhone    |     商户联系人电话      |                                |
|  addCountryCode   | 商户注册地址国家代码：默认CHN |                                |
| addCoProvinceCode |    商户注册地址省代码     |                                |
|   addCoCityCode   |    商户注册地址市代码     |                                |
|   addCoAreaCode   |    商户注册地址区县代码    |                                |
|    reqAddress     |      商户注册地址      |                                |
|     settleWay     |    商户结算途径，默认1    |              固定值               |
|    settleCycle    |    商户结算周期，默认0    |              固定值               |
|    chargeType     |   商户计费类型，默认00    |              固定值               |
|   chargingGrade   |    商户计费档次，默认0    |              固定值               |
|    accountName    |      结算账户名称      |                                |
|      bankNum      |      结算账户帐号      |                                |
| bankCodeThreeCode |    结算账户开户行代码     |                                |
|     bankName      |    结算账户开户行名称     |                                |
|     bankCode      |  结算账户开户行支付系统行号   |                                |
|  holidaysSettle   |     节假日合并结算      |             固定值：是              |
|    wxRfeeType     |  微信手续费收取方式，默认0   |                                |
|      wxRrate      |    微信手续费收取额度     |       填百分比，若0.6%则填写0.006       |
|      wxRtop       |    微信手续费额度封顶     |                                |
|    wxRrateMin     |    微信手续费额度保底     |                                |
|    zfbRfeeType    |  支付宝手续费收取方式，默认0  |                                |
|     zfbRrate      |    支付宝手续费收取额度    |       填百分比，若0.6%则填写0.006       |
|      zfbRtop      |    支付宝手续费额度封顶    |                                |
|    zfbRrateMin    |    支付宝手续费额度保底    |                                |

####返回字段：
|   字段名    |   字段描述   |     备注      |
| :------: | :------: | :---------: |
|  status  |  成功状态值   | success表示成功 |
|   msg    |   成功信息   |             |
| respDesc | 报备失败返回信息 |             |
| respCode |  失败错误码   |             |

> 例子：成功返回信息：{"data":"{"status":"success","msg":"1条推送成功"}"}

> 报备失败返回信息：{"data":"{"respDesc":"组织机构验证失败","respCode":"99"}"}

### 1.3.2  签到

#### 传入参数

|      字段名       |        字段描述        |    备注    |
| :------------: | :----------------: | :------: |
|    payPass     |   //1-微信， 2-支付宝    |          |
|    sendTime    | 发送时间yyyyMMddHHmmss |          |
|   sendSeqId    |       发送流水号        |          |
|   transType    |       交易类型码        | A001  固定 |
| organizationId |        机构号         |          |

#### 返回参数

|     字段名      |        字段描述        |    备注    |
| :----------: | :----------------: | :------: |
|   respDesc   |        说明字段        |          |
|   sendTime   | 发送时间yyyyMMddHHmmss |          |
|  sendSeqId   |       发送流水号        |          |
|  transType   |       交易类型码        | A001  固定 |
|   respCode   |        状态值         |          |
| terminalInfo |       mackey       |          |

> 返回例子：{"data":"{"sendTime":"20170821103242","sendSeqId":"ZFBTest_0006","respDesc":"签到成功","terminalInfo":"XXXXXXXXXXXXXXXXX","respCode":"00","transType":"A001"}"}


### 1.3.3请求二维码交易

#### 传入参数

|      字段名       |  字段描述   |           备注           |
| :------------: | :-----: | :--------------------: |
|    sendTime    |  发送时间   |     yyyyMMddHHmmss     |
|   sendSeqId    |  发送流水号  |                        |
|   transType    |  交易类型码  | 固定值 微信：B001   支付宝：Z001 |
| organizationId |   机构号   |                        |
|    payPass     |  支付通道   |      1-微信， 2-支付宝       |
|    transAmt    |  交易金额   |                        |
|      fee       |   手续费   | 单位分，具体扣去多少钱，根据报备费率做校验用 |
|     cardNo     |  结算卡号   |         商户校验字段         |
|      name      |  持卡人姓名  |         商户校验字段         |
|     idNum      | 持卡人身份证号 |         商户校验字段         |
|      body      |  商品描述   |                        |
|   notifyUrl    |  通知地址   |       支付成功的回调地址        |
|     mobile     | 收款方手机号  |         区分商户唯一         |
|      mac       |  加密mac  |                        |
|      qrflag       |  固态码 gdm， 动态码 dtm ， |      不参与mac值计算，不传默认固态码        |

#### 返回参数

|      字段名       |  字段描述  |           备注           |
| :------------: | :----: | :--------------------: |
|    sendTime    |  发送时间  |     yyyyMMddHHmmss     |
|   sendSeqId    | 发送流水号  |                        |
|   transType    | 交易类型码  | 固定值 微信：B001   支付宝：Z001 |
| organizationId |  机构号   |                        |
|    transAmt    |  交易金额  |                        |
|     imgUrl     | 交易支付链接 |                        |
|    respDesc    | 返回码描述  |                        |
|    respCode    |  返回码   |                        |

> 返回例子：{"data":"{"sendTime":"20170821103827","imgUrl":"http://trx.ronghuijinfubj.com/middlepaytrx/wx/authRedirect/PINGAN/PA1503283215346AQ611XF","transType":"B001","organizationId":"123123123","transAmt":"200","sendSeqId":"20170821103827123","respDesc":"获取成功","respCode":"00"}"}

### 1.3.3.1二维码交易回调接口说明

返回表单数据，key为data，值为以下json。

|     字段名      |    字段描述    |           备注           |
| :----------: | :--------: | :--------------------: |
|     fee      |   交易手续费    |                        |
|     mac      |    mac值    |                        |
| orgSendSeqId |    机构号     |                        |
|   payDesc    |   支付状态描述   |                        |
|  payResult   |   支付状态码    |       00 表示支付成功        |
|  t0RespCode  | t0交易到账状态码  |        00表示到账成功        |
|  t0RespDesc  | t0交易到账状态描述 |                        |
|   transAmt   |    交易金额    |          单位分           |
|  transType   |   交易类型码    | 固定值 微信：B001   支付宝：Z001 |

> 返回例子：{"fee":"1","mac":"32344642","orgSendSeqId":"ZFBTest_0044","organizationId":"15901101057","payDesc":"支付成功","payResult":"00","t0RespCode":"66","t0RespDesc":"代付状态未知","transAmt":"2","transType":"B001"}

### 1.3.4二维码交易查询

调用此接口会补发一次回调。

#### 传入参数

|    字段名    |  字段描述  |           备注           |
| :-------: | :----: | :--------------------: |
| sendSeqId | 发送流水号  |                        |
| transType | 交易类型码  | 固定值 微信：B001   支付宝：Z001 |
|  payType  | 支付渠道类型 | 微信：WeChat   支付宝：AliPay |

#### 返回参数

|     字段名      |  字段描述  |    备注    |
| :----------: | :----: | :------: |
| orgSendSeqId |  订单号   |          |
|  payResult   |  返回码   | 00代表支付成功 |
|   payDesc    | 支付状态描述 |          |

> 例子：{"data":"{"orgSendSeqId":"ZFBTest_0024","payResult":"99","payDesc":"未支付"}"}


### mac加密算法

```
加密方式 采用双倍长密钥对数据进行加密

加密算法

将报文中所有请求数据按照字段名的 ascii 码从小到大排序构成MAC ELEMEMENT BLOCK (MAB)。

b) 对MAB，按每8个字节做异或(不管信息中的字符格式)，如果最后不满8个字节， 则添加“0X00”。

示例:

MAB = M1 M2 M3 M4

其中:

M1 = MS11 MS12 MS13 MS14 MS15 MS16 MS17 MS18 M2 = MS21 MS22 MS23 MS24 MS25 MS26 MS27 MS28 M3 = MS31 MS32 MS33 MS34 MS35 MS36 MS37 MS38 M4 = MS41 MS42 MS43 MS44 MS45 MS46 MS47 MS48

按如下规则进行异或运算:

MS11 MS12 MS13 MS14 MS15 MS16 MS17 MS18

XOR) MS21 MS22 MS23 MS24 MS25 MS26 MS27 MS28 ---------------------------------------------------

TEMP BLOCK1 = TM11 TM12 TM13 TM14 TM15 TM16 TM17 TM18

然后，进行下一步的运算:

TM11 TM12 TM13 TM14 TM15 TM16 TM17 TM18 XOR) MS31 MS32 MS33 MS34 MS35 MS36 MS37 MS38 ---------------------------------------------------

TEMP BLOCK2 = TM21 TM22 TM23 TM24 TM25 TM26 TM27 TM28

再进行下一步的运算:

TM21 TM22 TM23 TM24 TM25 TM26 TM27 TM28 XOR) MS41 MS42 MS43 MS44 MS45 MS46 MS47 MS48 ---------------------------------------------------

RESULT BLOCK = TM31 TM32 TM33 TM34 TM35 TM36 TM37 TM38

c) 将异或运算后的最后8个字节(RESULT BLOCK)转换成16 个HEXDECIMAL: RESULT BLOCK = TM31 TM32 TM33 TM34 TM35 TM36 TM37 TM38

= TM311 TM312 TM321 TM322 TM331 TM332 TM341 TM342 || TM351 TM352 TM361 TM362 TM371 TM372 TM381 TM382

9

d) 取前8 个字节用MAK加密:

ENC BLOCK1 = eMAK(TM311 TM312 TM321 TM322 TM331 TM332 TM341 TM342)

= EN(1)1 EN(1)2 EN(1)3 EN(14) EN(1)5 EN(1)6 EN(1)7 EN(1)8

e) 将加密后的结果与后8 个字节异或:

EN(1)1 EN(1)2 EN(1)3 EN(14) EN(1)5 EN(1)6 EN(1)7 EN(1)8

XOR) TM351 TM352 TM361 TM362 TM371 TM372 TM381 TM382 ------------------------------------------------------------

TEMPBLOCK= TE11 TE12 TE13 TE14 TE15 TE16 TE17 TE18

f) 用异或的结果TEMP BLOCK 再进行一次单倍长密钥算法运算。 ENC BLOCK2 = eMAK(TE11 TE12 TE13 TE14 TE15 TE16 TE17 TE18)

= EN(2)1 EN(2)2 EN(2)3 EN(2)4 EN(2)5 EN(2)6 EN(2)7 EN(2)8

g) 将运算后的结果(ENC BLOCK2)转换成16 个HEXDECIMAL:

ENC BLOCK2 = EN(2)1 EN(2)2 EN(2)3 EN(2)4 EN(2)5 EN(2)6 EN(2)7 EN(2)8

= EM211 EM212 EM221 EM222 EM231 EM232 EM241 EM242 || EM251 EM252 EM261 EM262 EM271 EM272 EM281 EM282

示例:

ENC RESULT= %H84, %H56, %HB1, %HCD, %H5A, %H3F, %H84, %H84 转换成16 个HEXDECIMAL:

“8456B1CD5A3F8484”

h) 取前8个字节作为MAC值。 取”8456B1CD”为 MAC 值。
```

### 接入商家案例

​	请使用支付宝或者微信扫码测试

![0017072509403753](0017072509403753.png)