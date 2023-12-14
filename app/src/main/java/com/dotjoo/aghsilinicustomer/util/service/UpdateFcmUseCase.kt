package com.dotjoo.aghsilinicustomer.util.service

import com.dotjoo.aghsilinicustomer.data.Param.UpdateFcmTokenParam
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import com.dotjoo.aghsilinicustomer.data.Repository
import javax.inject.Inject

class UpdateFcmUseCase @Inject constructor (val repo: Repository ){
    suspend operator fun invoke() = repo.updateFcmToken(UpdateFcmTokenParam(PrefsHelper.getFcmToken()))
}