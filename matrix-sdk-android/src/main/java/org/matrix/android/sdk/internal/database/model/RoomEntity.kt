/*
 * Copyright 2020 The Matrix.org Foundation C.I.C.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.matrix.android.sdk.internal.database.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.internal.database.model.threads.ThreadSummaryEntity
import org.matrix.android.sdk.internal.database.query.findRootOrLatest
import org.matrix.android.sdk.internal.extensions.assertIsManaged

internal open class RoomEntity(@PrimaryKey var roomId: String = "",
                               var chunks: RealmList<ChunkEntity> = RealmList(),
                               var sendingTimelineEvents: RealmList<TimelineEventEntity> = RealmList(),
                               var threadSummaries: RealmList<ThreadSummaryEntity> = RealmList(),
                               var accountData: RealmList<RoomAccountDataEntity> = RealmList()
) : RealmObject() {

    private var membershipStr: String = Membership.NONE.name
    var membership: Membership
        get() {
            return Membership.valueOf(membershipStr)
        }
        set(value) {
            membershipStr = value.name
        }

    private var membersLoadStatusStr: String = RoomMembersLoadStatusType.NONE.name
    var membersLoadStatus: RoomMembersLoadStatusType
        get() {
            return RoomMembersLoadStatusType.valueOf(membersLoadStatusStr)
        }
        set(value) {
            membersLoadStatusStr = value.name
        }
    companion object
}
internal fun RoomEntity.removeThreadSummaryIfNeeded(eventId: String) {
    assertIsManaged()
    threadSummaries.findRootOrLatest(eventId)?.let {
        threadSummaries.remove(it)
        it.deleteFromRealm()
    }
}
