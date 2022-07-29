package com.hitqz.disinfectionrobot.data;


public class CreateMapPosInfoDto {

    private CreateMap2DPoseDto poseDto;

    public CreateMap2DPoseDto getPoseDto() {
        return poseDto;
    }

    public void setPoseDto(CreateMap2DPoseDto poseDto) {
        this.poseDto = poseDto;
    }

    @Override
    public String toString() {
        return "CreateMapPosInfoDto{" +
                "poseDto=" + poseDto +
                '}';
    }
}
