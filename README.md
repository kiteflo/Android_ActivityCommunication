Android Activity communication
=================================

Passing values between Android activities sometimes just feels like fucking pain in the ass. You can use a global context but this somehow feels "dirty" as using a global session pretty much reminds me of first Servlet engineering when discovering the session context in terms of "cool, I can put anything there...". 

This Mini Codebase chalks out a clear way (the Android recommended way of so called "inter-activity-communication") - and it's really simple, you just need to know how this basically should work. And yes, I know: there is an "excellent" Android documentation in place but unfortunately these guys always treat the one-way-communication case only in terms of ActivityA notifies/launches ActivityB but there is no example chalking out the whole roundtrip...well, here is.

![ScreenShot](/images/example_small.png)

# What do we do here?

The example is simple: there are two activities (MainActivity and SecondActivity), the first activity launches the send one and passes in a value. Next the second activity is closed and passes a value back to the first activity...et voila.

## Android inter activity communication "MainActivity"

The most important point is, when launching the SecondActivity this needs to be done using the **startActivityForResult** method  and not the **startActivity** method!

```java
/**
 * Launch SecondActivity via intent and pass value to SecondActivity...
 * @param view
 */
public void onLaunchSecondActivity(View view)
{
    Intent secIntent = new Intent(this,SecondActivity.class);
    EditText et = (EditText)findViewById(R.id.et_first_passin);
    String name = et.getText().toString();
    secIntent.putExtra("PASSIN",name);

    startActivityForResult(secIntent,0);
}
```

This enables you to specify a callback handler within your MainActivity, the call back will be triggered as soon as the SecondActivity is finished, and surprise surprise...when overriding the callback intent data is passed into the method:

```java
/**
 * Handle activity result...
 * @param requestCode
 * @param resultCode
 * @param data
 */
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data)
{
    String value = data.getExtras().getString("PASSBACK");
    EditText et = (EditText)findViewById(R.id.et_first_passback);
    et.setText(value);
}
```

That's all you need to apply to the MainActivity, coding the SecondActivity is pretty straight forward:

## Android inter activity communication "SecondActivity"

Note: we are using fragments for view encapsulation, dont worry about this additional layer you can do all we're doing in our fragment within your activity the same way...we prefer the fragment way.

Ok, first thing we need to do is catching the intent in order to read the data MainActivity passes in and apply this data to our view:

```java
@Override
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);

    // fetch intent value(s) and apply to fragment...
    Intent trigger = getIntent();
    String value = trigger.getExtras().getString("PASSIN");
    FragmentSecond second = new FragmentSecond();
    second.setPassInValue(value);

    setContentView(R.layout.activity_second);

    if (savedInstanceState == null) 
    {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, second)
                .commit();
    }
}
```

Second and final step is passing data back to MainActivity, when using the **finish()** method this ensures our MainActivity callback is triggered:

```java
/**
 * Finish activity and rise intent in order to notify FirstActivity the
 * activity has been finished....
 * @param view
 */
public void onBackToFirstActivity(View view)
{
    EditText et = (EditText)findViewById(R.id.et_second_passback);
    Intent returnIntent = new Intent();
    returnIntent.putExtra("PASSBACK", et.getText().toString());
    setResult(RESULT_OK, returnIntent);
    finish();
}
```

Done!!! You notice the **setResult** method? That's exactly the magic enabling us to pass in any data which than can be used within our MainActivity...that's the best and Android recommended way of handling inter activity communication, hurra!

## Conclusion

Passing data between activities is pretty simple. Using the recommended Android way we can ensure that the activity is 100%ly closed using finisehd method, of course when heading for a stateful solution other patterns might be applied. Maybe I'm gonna extend this example next couple of days in order to handle a stateful Activity launch/resume - stay tuned!