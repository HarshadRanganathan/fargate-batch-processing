# Step Functions

Create a new state machine and add the definition from `definition.json` file. Update region, account id and subnet details in the definition appropriately.

Create or use an existing role which will have access to perform ECS task management and also iamPassRole to be able to assign role for the container task.

Start an execution with below input where source refers to the S3 file that needs to be copied:

```
{
  "commands": [
    "--source=s3://",
    "--destination=s3://"
  ]
}
```

Additional References:

[1] https://docs.aws.amazon.com/step-functions/latest/dg/limits.html