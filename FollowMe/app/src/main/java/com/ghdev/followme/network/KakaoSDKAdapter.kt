package com.ghdev.followme.network

import com.kakao.auth.*

class KakaoSDKAdapter : KakaoAdapter() {
    //ISessionConfig는 Session에 대한 옵션 값들을 설정할 수 있음.
    //Session은 로그인 객체를 유지 시키는 객체로 Access Token을 관리하는 역할.
    override fun getSessionConfig(): ISessionConfig {
        return object : ISessionConfig {
            override fun getAuthTypes(): Array<AuthType> {
                return arrayOf(AuthType.KAKAO_ACCOUNT)
            }

            override fun isUsingWebviewTimer(): Boolean {
                return false
            }//CPU 소모를 절약할지의 여부

            override fun getApprovalType(): ApprovalType? {
                return ApprovalType.INDIVIDUAL
            }//kakao와 제휴된 앱에서 사용되는 값.

            override fun isSaveFormData(): Boolean {
                return true
            }//email 입력 폼의 데이터를 저장할 지 여부 지정

            override fun isSecureMode(): Boolean {
                return true
            }//로그인 시 토큰을 저장할 때 암호화 여부
        }
    }

    //IApplicationConfig는 어플이 가지고 있는 정보를 전다하는 역할
    override fun getApplicationConfig(): IApplicationConfig {
        return IApplicationConfig {
            ApplicationController.instance?.getGlobalApplicationContext()
        }
    }
}