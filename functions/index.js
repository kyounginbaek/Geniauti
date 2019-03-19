/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
const app = admin.initializeApp();
const db = admin.firestore();

/**
 * Triggers when a user gets a new follower and sends a notification.
 *
 * Followers add a flag to `/followers/{followedUid}/{followerUid}`.
 * Users save their device notification tokens to `/users/{followedUid}/notificationTokens/{notificationToken}`.
 */

exports.sendNotification = functions.firestore
    .document('behaviors/{behaviorId}')
    .onCreate((snap, context) => {

      const newValue = snap.data();

      let uid = newValue.uid;
      let cid = newValue.cid;

      // The snapshot to the user's tokens.
      let tokensSnapshot;
      let tokens_to_send_to;
      let getDeviceTokensPromise;

      db.doc(`users/${uid}`).get().then((result => {

	    getDeviceTokensPromise = result.data().notificationTokens;
        
        db.doc(`childs/${cid}`).get().then((result => {

		    tokensSnapshot = result.data().users;
	        tokens_to_send_to = [];

	        console.log("작동중");

	        for(var i in tokensSnapshot) {
	        	var map = tokensSnapshot[i];

	        	for(var obj in map){
	        		if (map.hasOwnProperty(obj)) {
				        if(obj === 'notificationTokens') {
				        	if(obj !== getDeviceTokensPromise) {
				        		console.log(map[obj]);
				        		tokens_to_send_to.push(map[obj]);
				        	}
				        }
				    }
	        	}
	        }

		    // Notification details.
	        const payload = {
	          notification: {
	            title: '지니어티(Geniauti) 알림',
	            body: `아이의 새로운 행동 기록이 추가되었습니다.`,
	            // icon: follower.photoURL
	          }
	        };

		    // Send notifications to all tokens.
		    if(tokens_to_send_to.length === 0) {
		    	return null;
		    } else {
		    	return admin.messaging().sendToDevice(tokens_to_send_to, payload);
		    }
	       

			})).catch((error) => {
		    	console.log(error);
		    })

		return null;

		})).catch((error) => {
	    	console.log(error);
	    })

	    return null;
    
    });

// exports.sendFollowerNotification = functions.database.ref('/followers/{followedUid}/{followerUid}')
//     .onWrite((change, context) => {
//       const followerUid = context.params.followerUid;
//       const followedUid = context.params.followedUid;
//       // If un-follow we exit the function.
//       if (!change.after.val()) {
//         return console.log('User ', followerUid, 'un-followed user', followedUid);
//       }
//       console.log('We have a new follower UID:', followerUid, 'for user:', followedUid);

//       // Get the list of device notification tokens.
//       const getDeviceTokensPromise = admin.database()
//           .ref(`/users/${followedUid}/notificationTokens`).once('value');

//       // Get the follower profile.
//       const getFollowerProfilePromise = admin.auth().getUser(followerUid);

//       // The snapshot to the user's tokens.
//       let tokensSnapshot;

//       // The array containing all the user's tokens.
//       let tokens;

//       return Promise.all([getDeviceTokensPromise, getFollowerProfilePromise]).then(results => {
//         tokensSnapshot = results[0];
//         const follower = results[1];

//         // Check if there are any device tokens.
//         if (!tokensSnapshot.hasChildren()) {
//           return console.log('There are no notification tokens to send to.');
//         }
//         console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
//         console.log('Fetched follower profile', follower);

//         // Notification details.
//         const payload = {
//           notification: {
//             title: 'You have a new follower!',
//             body: `${follower.displayName} is now following you.`,
//             icon: follower.photoURL
//           }
//         };

//         // Listing all tokens as an array.
//         tokens = Object.keys(tokensSnapshot.val());
//         // Send notifications to all tokens.
//         return admin.messaging().sendToDevice(tokens, payload);
//       }).then((response) => {
//         // For each message check if there was an error.
//         const tokensToRemove = [];
//         response.results.forEach((result, index) => {
//           const error = result.error;
//           if (error) {
//             console.error('Failure sending notification to', tokens[index], error);
//             // Cleanup the tokens who are not registered anymore.
//             if (error.code === 'messaging/invalid-registration-token' ||
//                 error.code === 'messaging/registration-token-not-registered') {
//               tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
//             }
//           }
//         });
//         return Promise.all(tokensToRemove);
//       });
//     });