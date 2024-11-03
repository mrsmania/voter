import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { PollComponent } from './components/poll/poll.component';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'poll/:token', component: PollComponent }, // Route für den Poll
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
