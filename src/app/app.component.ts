import { Component } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Voice Assistant';
  recording = false;
  transcript = '';
  mediaRecorder!: MediaRecorder;
  audioChunks: any[] = [];

  constructor(private http: HttpClient) {}

  startRecording() {
    this.recording = true;
    this.transcript = '';

    navigator.mediaDevices.getUserMedia({ audio: true })
      .then(stream => {
        this.mediaRecorder = new MediaRecorder(stream);
        this.audioChunks = [];

        this.mediaRecorder.ondataavailable = event => {
          this.audioChunks.push(event.data);
        };

        this.mediaRecorder.onstop = () => {
          const audioBlob = new Blob(this.audioChunks, { type: 'audio/webm' });
          this.sendAudio(audioBlob);
        };

        this.mediaRecorder.start();
      })
      .catch(err => {
        console.error('Microphone error:', err);
        this.recording = false;
      });
  }

  stopRecording() {
    if (this.mediaRecorder && this.mediaRecorder.state !== 'inactive') {
      this.mediaRecorder.stop();
      this.recording = false;
    }
  }

  sendAudio(audioBlob: Blob) {
    const formData = new FormData();
    formData.append('file', audioBlob, 'voice.webm');

    this.http.post<{ transcript: string }>('http://localhost:8080/transcribe', formData)
      .subscribe({
        next: (res) => {
          this.transcript = res.transcript;
        },
        error: (err) => {
          console.error('Upload failed:', err);
          this.transcript = 'Error: Could not transcribe audio.';
        }
      });
  }
}
