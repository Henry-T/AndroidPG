.class Lcom/henryt/LifeCycle/MyActivity$1;
.super Ljava/lang/Object;
.source "MyActivity.java"

# interfaces
.implements Landroid/view/View$OnClickListener;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/henryt/LifeCycle/MyActivity;->onCreate(Landroid/os/Bundle;)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Lcom/henryt/LifeCycle/MyActivity;


# direct methods
.method constructor <init>(Lcom/henryt/LifeCycle/MyActivity;)V
    .locals 0

    iput-object p1, p0, Lcom/henryt/LifeCycle/MyActivity$1;->this$0:Lcom/henryt/LifeCycle/MyActivity;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onClick(Landroid/view/View;)V
    .locals 10

    new-instance v2, Landroid/content/Intent;

    iget-object v4, p0, Lcom/henryt/LifeCycle/MyActivity$1;->this$0:Lcom/henryt/LifeCycle/MyActivity;

    const-class v5, Lcom/henryt/LifeCycle/MyActivity;

    invoke-direct {v2, v4, v5}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    const v1, 0x50466cd

    iget-object v4, p0, Lcom/henryt/LifeCycle/MyActivity$1;->this$0:Lcom/henryt/LifeCycle/MyActivity;

    const/high16 v5, 0x10000000

    invoke-static {v4, v1, v2, v5}, Landroid/app/PendingIntent;->getActivity(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;

    move-result-object v0

    iget-object v4, p0, Lcom/henryt/LifeCycle/MyActivity$1;->this$0:Lcom/henryt/LifeCycle/MyActivity;

    const-string v5, "alarm"

    invoke-virtual {v4, v5}, Lcom/henryt/LifeCycle/MyActivity;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/app/AlarmManager;

    const/4 v4, 0x1

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v6

    const-wide/16 v8, 0x64

    add-long/2addr v6, v8

    invoke-virtual {v3, v4, v6, v7, v0}, Landroid/app/AlarmManager;->set(IJLandroid/app/PendingIntent;)V

    const/4 v4, 0x0

    invoke-static {v4}, Ljava/lang/System;->exit(I)V

    return-void
.end method
