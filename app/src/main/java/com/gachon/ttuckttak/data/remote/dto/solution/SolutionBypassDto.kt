package com.gachon.ttuckttak.data.remote.dto.solution

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SolutionBypassDto(
    val bypassIdx: String?,
    val startEntryIdx: Int,
    val targetEntryIdx: Int,
    val targetEntryName: String?
): Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<SolutionBypassDto> = object : Parcelable.Creator<SolutionBypassDto> {
            override fun createFromParcel(source: Parcel): SolutionBypassDto = SolutionBypassDto(source)
            override fun newArray(size: Int): Array<SolutionBypassDto?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel): this(source.readString(), source.readInt(), source.readInt(), source.readString())
    override fun describeContents(): Int = 0

    override fun writeToParcel(p0: Parcel, flags: Int) {
        p0.writeString(bypassIdx)
        p0.writeInt(startEntryIdx)
        p0.writeInt(targetEntryIdx)
        p0.writeString(targetEntryName)
    }
}