/*
 * Copyright (c) 2022 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.app.features.location.live

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import im.vector.app.R
import im.vector.app.core.glide.GlideApp
import im.vector.app.core.utils.TextUtils
import im.vector.app.databinding.ViewLocationLiveMessageBannerBinding
import im.vector.app.features.themes.ThemeUtils
import org.threeten.bp.Duration

data class LocationLiveMessageBannerViewState(
        val isStopButtonVisible: Boolean,
        val remainingTimeInMillis: Long,
        val bottomStartCornerRadiusInDp: Float,
        val bottomEndCornerRadiusInDp: Float,
)

class LocationLiveMessageBannerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewLocationLiveMessageBannerBinding.inflate(
            LayoutInflater.from(context),
            this
    )

    val stopButton: Button
        get() = binding.locationLiveMessageBannerStop

    private val background: ImageView
        get() = binding.locationLiveMessageBannerBackground

    private val subTitle: TextView
        get() = binding.locationLiveMessageBannerSubTitle

    fun render(viewState: LocationLiveMessageBannerViewState) {
        stopButton.isVisible = viewState.isStopButtonVisible
        val duration = Duration.ofMillis(viewState.remainingTimeInMillis.coerceAtLeast(0L))
        subTitle.text = context.getString(R.string.location_share_live_remaining_time, TextUtils.formatDurationWithUnits(context, duration))
        GlideApp.with(context)
                .load(ColorDrawable(ThemeUtils.getColor(context, R.attr.colorSurface)))
                .transform(GranularRoundedCorners(0f, 0f, viewState.bottomEndCornerRadiusInDp, viewState.bottomStartCornerRadiusInDp))
                .into(background)
    }
}
