import {
  trigger,
  transition,
  style,
  query,
  group,
  animateChild,
  animate,
  keyframes,
} from '@angular/animations';
export const fader =
  trigger('routeAnimations', [
    transition('* <=> *', [
      // Set a default  style for enter and leave
      query(':leave', [
        style({
          position: 'absolute',
          left: 0,
        }),
      ], { optional: true }),
      query(':enter, :leave', [
        style({
          // position: 'absolute',
          // left: 0,
          // width: '100%',
          opacity: 0,
        }),
      ], { optional: true }),
      // Animate the new page in
      query(':enter', [
        animate('300ms ease-in-out', style({ opacity: 1 })),
      ], { optional: true })
    ]),
  ]);
