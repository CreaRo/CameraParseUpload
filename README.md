# CameraParseUpload

I was asked to create a selfie application while applying to SocialCops. It allows you to take a selfie, and later upload to Parse.
I have implemented my own camera, and am uploading images and videos in the background. I have followed material design guidelines.

Here are a few pointers while going through the source code.

Parse key and ID can be changed in utils.ParseApplication.
The photos and videos clicked from CapturePhotoActivity and CaptureVideoActivity are saved to the memory. The path is specified in BasePath. (Currently /CameraParse, /CameraParse/thumbnails)

The adapter initially took too much time to load the images, even after I used a recylerview with a holder. This was because the images took too long to resize. Hence I created the thumbnails directory storing images of ~8kB.

NotificationMaker makes the notifications. Each upload does not get its own notification ID currently.

Initially I created a service myself to run the async upload myself, though later realised Parse does this on its own. However, the upload stops and does not resume if you close the application. Continues if you pause it though.

This is the first time Ive created a camera app, and have not been able to test how the app works on low-end devices.